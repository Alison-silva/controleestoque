package com.alison.controleestoque.repositories;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alison.controleestoque.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query("select u from Produto u where u.nome like %?1%")
	List<Produto> findProdutoByNome(String nome);
	
	default Page<Produto> findProdutoByNomePage(String nome, PageRequest pageRequest) {

		Produto produto = new Produto();
		produto.setNome(nome);

		/* Configurando para pesquisar por nome e paginação */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		Example<Produto> example = Example.of(produto, exampleMatcher);

		Page<Produto> retorno = findAll(example, pageRequest);

		return retorno;

	}

}
