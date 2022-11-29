package com.alison.controleestoque.repositories;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alison.controleestoque.model.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>{
	
	@Query("select u from Fornecedor u where u.razaoSocial like %?1%")
	List<Fornecedor> findFornByRazaoSocial(String razaoSocial);
	
	default Page<Fornecedor> findFornByRazaoSocialPage(String razaoSocial, PageRequest pageRequest) {

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setRazaoSocial(razaoSocial);

		/* Configurando para pesquisar por nome e paginação */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("razaoSocial",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		Example<Fornecedor> example = Example.of(fornecedor, exampleMatcher);

		Page<Fornecedor> retorno = findAll(example, pageRequest);

		return retorno;

	}

}
