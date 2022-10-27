package com.alison.controleestoque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alison.controleestoque.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
