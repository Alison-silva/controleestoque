package com.alison.controleestoque.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alison.controleestoque.model.Usuario;
import com.alison.controleestoque.repositories.UsuarioRepository;
import com.alson.controleestoque.service.ImplementacaoUserDetailsService;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	
	@PostMapping(value = "salvaUsuario")
	@ResponseBody
	public ResponseEntity<Usuario> salvaUsuario(@RequestBody @Valid Usuario usuario){
		
		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		Usuario usua = usuarioRepository.save(usuario);
		implementacaoUserDetailsService.insereAcessoPadrao(usua.getId());
		return new ResponseEntity<Usuario>(usua, HttpStatus.OK);
	}
	
	@PutMapping(value = "atualizaUsuario")
	@ResponseBody
	public ResponseEntity<?> atualizaUsuario(@RequestBody Usuario usuario){
		if(usuario.getId() == null) {
			return new ResponseEntity<String>("Id não foi informado!", HttpStatus.OK);
		}
		
		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();
		
		if (!userTemporario.getSenha().equals(usuario.getSenha())) { 
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}
		
		Usuario usua = usuarioRepository.saveAndFlush(usuario);
		return new ResponseEntity<Usuario>(usua, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "deletaUsuario/{id}")
	@ResponseBody
	public String deletaUsuario(@PathVariable("id") Long id) {
		usuarioRepository.deleteById(id);
		return "Deletado com sucesso!";		
	}
	
	@GetMapping(value = "listarUsuario", produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<Usuario>> listarUsuario(){
		List<Usuario> usua = usuarioRepository.findAll();
		return new ResponseEntity<List<Usuario>>(usua, HttpStatus.OK);
	}
	
}







