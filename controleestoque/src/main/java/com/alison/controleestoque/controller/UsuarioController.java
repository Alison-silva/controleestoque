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
import com.alison.controleestoque.service.ImplementacaoUserDetailsService;

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
	
	@PutMapping(value = "atualizaUsuario", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Usuario> atualizaUsuario(@RequestBody Usuario usuario){
		
		
		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();
		
		if (!userTemporario.getSenha().equals(usuario.getSenha())) { 
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}
		
		Usuario usua = usuarioRepository.save(usuario);
		
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
	public ResponseEntity<Page<Usuario>> listarUsuario(){
		
		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
		
		Page<Usuario> list = usuarioRepository.findAll(page);
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	@GetMapping(value = "listarUsuario/page/{pagina}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Usuario>> listarUsuarioPagina(@PathVariable("pagina") int pagina){
		
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		
		Page<Usuario> list = usuarioRepository.findAll(page);
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "listarUsuarioPorNome/{nome}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Usuario>> listarUsuarioPorNome(@PathVariable("nome") String nome){
		
		PageRequest pageRequest = null;
		Page<Usuario> list = null;

		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
		}

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	@GetMapping(value = "listarUsuarioPorNomePage/{nome}/page/{page}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Usuario>> listarUsuarioPorNomePage(@PathVariable("nome") String nome, @PathVariable("page") int page){
		
		PageRequest pageRequest = null;
		Page<Usuario> list = null;

		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			list = usuarioRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
		}

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "buscaPorId/{id}", produces = "application/json")
	public ResponseEntity<Usuario> buscarPorId(@PathVariable (value = "id") Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
}




















