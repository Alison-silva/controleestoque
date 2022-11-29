package com.alison.controleestoque.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alison.controleestoque.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

	@Query("select u from Usuario u where u.nome like %?1%")
	List<Usuario> findUserByNome(String nome);

	@Query(value = "SELECT constraint_name from information_schema.constraint_column_usage  where table_name = 'usuarios_role' and column_name = 'role_id' and constraint_name <> 'unique_role_user';", nativeQuery = true)
	String consultaConstraintRole();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_role (usuario_id, role_id) values(?1, (select id from role where nome_role = 'FUNCIONARIO')); ")
	void insereAcessoRolePadrao(Long idUser);
	
	default Page<Usuario> findUserByNamePage(String nome, PageRequest pageRequest) {

		Usuario usuario = new Usuario();
		usuario.setNome(nome);

		/* Configurando para pesquisar por nome e paginação */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		Example<Usuario> example = Example.of(usuario, exampleMatcher);

		Page<Usuario> retorno = findAll(example, pageRequest);

		return retorno;

	}


}
