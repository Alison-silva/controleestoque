package com.alison.controleestoque.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.alison.controleestoque.model.Usuario;
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
	public ResponseEntity<Page<Fornecedor>> listarFornecedor(){
		PageRequest page = PageRequest.of(0, 5, Sort.by("razaoSocial"));
		Page<Fornecedor> forn = fornecedorRepository.findAll(page);
		return new ResponseEntity<Page<Fornecedor>>(forn, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "listarFornecedor/page/{pagina}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Fornecedor>> listarFornecedorPagina(@PathVariable("pagina") int pagina){
		
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("razaoSocial"));
		
		Page<Fornecedor> list = fornecedorRepository.findAll(page);
		
		return new ResponseEntity<Page<Fornecedor>>(list, HttpStatus.OK);
	}
	
	@GetMapping(value = "listarFornecedorPorRazao/{razaoSocial}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Fornecedor>> listarFornecedorPorRazao(@PathVariable("razaoSocial") String razaoSocial){
		
		PageRequest pageRequest = null;
		Page<Fornecedor> list = null;

		if (razaoSocial == null || (razaoSocial != null && razaoSocial.trim().isEmpty())
				|| razaoSocial.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(0, 5, Sort.by("razaoSocial"));
			list = fornecedorRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(0, 5, Sort.by("razaoSocial"));
			list = fornecedorRepository.findFornByRazaoSocialPage(razaoSocial, pageRequest);
		}

		return new ResponseEntity<Page<Fornecedor>>(list, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "listarFornecedorPorRazaoPage/{razaoSocial}/page/{page}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Fornecedor>> listarFornecedorPorRazaoPage(@PathVariable("razaoSocial") String razaoSocial, @PathVariable("page") int page){
		
		PageRequest pageRequest = null;
		Page<Fornecedor> list = null;

		if (razaoSocial == null || (razaoSocial != null && razaoSocial.trim().isEmpty())
				|| razaoSocial.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(page, 5, Sort.by("razaoSocial"));
			list = fornecedorRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(page, 5, Sort.by("razaoSocial"));
			list = fornecedorRepository.findFornByRazaoSocialPage(razaoSocial, pageRequest);
		}

		return new ResponseEntity<Page<Fornecedor>>(list, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "buscaPorId/{id}", produces = "application/json")
	public ResponseEntity<Fornecedor> buscarPorId(@PathVariable (value = "id") Long id){
		Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
		return new ResponseEntity<Fornecedor>(fornecedor.get(), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "listaDeFornecedores", produces = "application/json")
	public ResponseEntity<List<Fornecedor>> fornecedor() {
		List<Fornecedor> lista = fornecedorRepository.findAll();
		return new ResponseEntity<List<Fornecedor>>(lista, HttpStatus.OK);
	}
	
}


























