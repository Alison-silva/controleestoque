package com.alison.controleestoque;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alison.controleestoque.controller.FornecedorController;
import com.alison.controleestoque.controller.ProdutoController;
import com.alison.controleestoque.model.Fornecedor;
import com.alison.controleestoque.model.Produto;
import com.alison.controleestoque.repositories.FornecedorRepository;
import com.alison.controleestoque.repositories.ProdutoRepository;

import junit.framework.TestCase;

@SpringBootTest(classes = ControleestoqueApplication.class)
public class TesteUnitarioSalvaProduto extends TestCase  {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private FornecedorController fornecedorController;
	
	@Autowired
	private ProdutoController produtoController;
	
	@Test
	public void SalvarProduto() {
		
		
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setRazaoSocial("Teste do fornecedor");
		fornecedor.setNomeFantasia("Loja de Testes LTDA");
		fornecedor.setCnpj("1234567891255");
		fornecedor.setInscricaoEstadual("1236547888880");
		fornecedor.setTelefone("559963632155");
		fornecedor.setCidade("São Borja");
		fornecedor.setEstado("RS");
		
		fornecedorRepository.save(fornecedor);
		
		
		Produto produto = new Produto();
		produto.setNome("Nome Teste");
		produto.setDescricao("O produto de Testes é bom");
		produto.setPreco(BigDecimal.TEN);
		produto.setQuantidade(5);
		produto.setFornecedor(fornecedor);
		
		produtoRepository.save(produto);
		
		Produto prodBusca = produtoRepository.findById(produto.getId()).get();
		
		System.out.println(prodBusca);
		
		assertEquals(produto.getId(), prodBusca.getId());
		
		
	}
	
	
	
}













