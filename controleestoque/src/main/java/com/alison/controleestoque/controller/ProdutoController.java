package com.alison.controleestoque.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alison.controleestoque.model.Produto;
import com.alison.controleestoque.repositories.ProdutoRepository;

@RestController
@RequestMapping(value = "/produto")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	
	@PostMapping(value = "salvaProduto")
	@ResponseBody
	public ResponseEntity<Produto> salvaProduto(@RequestBody @Valid Produto produto){
		Produto prod = produtoRepository.save(produto);
		return new ResponseEntity<Produto>(prod, HttpStatus.OK);
	}
	
	@PutMapping(value = "atualizaProduto")
	@ResponseBody
	public ResponseEntity<?> atualizaProduto(@RequestBody Produto produto){
		if(produto.getId() == null) {
			return new ResponseEntity<String>("Id não foi informado!", HttpStatus.OK);
		}
		Produto prod = produtoRepository.saveAndFlush(produto);
		return new ResponseEntity<Produto>(prod, HttpStatus.OK);
	}

	@DeleteMapping(value = "deletaProduto/{id}")
	@ResponseBody
	public String deletaProduto(@PathVariable("id") Long id) {
		produtoRepository.deleteById(id);
		return "Deletado com sucesso!";		
	}
	
	@GetMapping(value = "listarProduto")
	@ResponseBody
	public ResponseEntity<List<Produto>> listarProduto(){
		List<Produto> prod = produtoRepository.findAll();
		return new ResponseEntity<List<Produto>>(prod, HttpStatus.OK);
	}
	

}










