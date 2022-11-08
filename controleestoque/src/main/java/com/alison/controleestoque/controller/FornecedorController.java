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

import com.alison.controleestoque.model.Fornecedor;
import com.alison.controleestoque.repositories.FornecedorRepository;

@RestController
@RequestMapping(value = "/fornecedor")
public class FornecedorController {
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@PostMapping(value = "salvaFornecedor")
	@ResponseBody
	public ResponseEntity<Fornecedor> salvaFornecedor(@RequestBody @Valid Fornecedor fornecedor){
		Fornecedor forn = fornecedorRepository.save(fornecedor);
		return new ResponseEntity<Fornecedor>(forn, HttpStatus.OK);
	}
	
	
	@PutMapping(value = "atualizaFornecedor")
	@ResponseBody
	public ResponseEntity<?> atualizaFornecedor(@RequestBody Fornecedor fornecedor){
		if(fornecedor.getId() == null) {
			return new ResponseEntity<String>("Id não foi informado!", HttpStatus.OK);
		}
		Fornecedor forn = fornecedorRepository.saveAndFlush(fornecedor);
		return new ResponseEntity<Fornecedor>(forn, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "deletaFornecedor/{id}")
	@ResponseBody
	public String deletaFornecedor(@PathVariable("id") Long id) {
		fornecedorRepository.deleteById(id);
		return "Deletado com sucesso!";		
	}
	
	@GetMapping(value = "listarFornecedor", produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<Fornecedor>> listarFornecedor(){
		List<Fornecedor> forn = fornecedorRepository.findAll();
		return new ResponseEntity<List<Fornecedor>>(forn, HttpStatus.OK);
	}
	
	
}


























