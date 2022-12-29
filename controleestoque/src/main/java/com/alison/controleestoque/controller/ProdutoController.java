package com.alison.controleestoque.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alison.controleestoque.model.ProdChart;
import com.alison.controleestoque.model.Produto;
import com.alison.controleestoque.repositories.ProdutoRepository;
import com.alison.controleestoque.service.ServiceRelatorio;

@RestController
@RequestMapping(value = "/produto")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ServiceRelatorio serviceRelatorio;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping(value = "salvaProduto")
	@ResponseBody
	public ResponseEntity<Produto> salvaProduto(@RequestBody @Valid Produto produto) {
		Produto prod = produtoRepository.save(produto);
		return new ResponseEntity<Produto>(prod, HttpStatus.OK);
	}

	@PutMapping(value = "atualizaProduto")
	@ResponseBody
	public ResponseEntity<?> atualizaProduto(@RequestBody Produto produto) {
		if (produto.getId() == null) {
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

	@GetMapping(value = "listarProduto", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Produto>> listarProduto() {
		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
		Page<Produto> prod = produtoRepository.findAll(page);
		return new ResponseEntity<Page<Produto>>(prod, HttpStatus.OK);
	}

	@GetMapping(value = "listarProduto/page/{pagina}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Produto>> listarProdutoPagina(@PathVariable("pagina") int pagina) {
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		Page<Produto> list = produtoRepository.findAll(page);
		return new ResponseEntity<Page<Produto>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "listarProdutoPorNome/{nome}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Produto>> listarProdutoPorNome(@PathVariable("nome") String nome) {

		PageRequest pageRequest = null;
		Page<Produto> list = null;

		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = produtoRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = produtoRepository.findProdutoByNomePage(nome, pageRequest);
		}

		return new ResponseEntity<Page<Produto>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "listarProdutoPorNomePage/{nome}/page/{page}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Page<Produto>> listarProdutoPorNomePage(@PathVariable("nome") String nome,
			@PathVariable("page") int page) {

		PageRequest pageRequest = null;
		Page<Produto> list = null;

		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			list = produtoRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			list = produtoRepository.findProdutoByNomePage(nome, pageRequest);
		}

		return new ResponseEntity<Page<Produto>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/relatorio", produces = "application/text")
	public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
		byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-produtos-estoque", new HashMap(),
				request.getServletContext());

		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);

	}

	@GetMapping(value = "buscaPorId/{id}", produces = "application/json")
	public ResponseEntity<Produto> buscarPorId(@PathVariable(value = "id") Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		return new ResponseEntity<Produto>(produto.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/grafico", produces = "application/json")
	public ResponseEntity<ProdChart> grafico() {

		ProdChart prodChart = new ProdChart();

		List<String> resultado = jdbcTemplate.queryForList(
				"select array_agg(nome) from produto where quantidade > 0 and nome <> '' union all select cast(array_agg(quantidade) as character varying[]) from produto where quantidade > 0 and nome <> ''",
				String.class);
		
		System.out.println(resultado);

		if (!resultado.isEmpty()) {
			String nomes = resultado.get(0).replaceAll("\\{", "").replaceAll("\\}", "");
			String qtd = resultado.get(1).replaceAll("\\{", "").replaceAll("\\}", "");
			
			System.out.println(nomes);
			System.out.println(qtd);

			prodChart.setNome(nomes);
			prodChart.setQuantidade(qtd);
		}

		return new ResponseEntity<ProdChart>(prodChart, HttpStatus.OK);

	}

}
