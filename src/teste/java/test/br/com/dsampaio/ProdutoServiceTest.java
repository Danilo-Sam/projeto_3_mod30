package test.br.com.dsampaio;

import br.com.dsampaio.dao.ProdutoDAO;
import br.com.dsampaio.domain.Produto;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionElementoNaoConhecido;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;
import br.com.dsampaio.services.IProdutoService;
import br.com.dsampaio.services.ProdutoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.dsampaio.dao.IProdutoDAO;

public class ProdutoServiceTest {

    private IProdutoService produtoService;
    private Produto produto;

    public ProdutoServiceTest() {
        IProdutoDAO dao = new ProdutoDAO(); // Use ProdutoDAO real
        produtoService = new ProdutoService(dao);
    }

    @Before
    public void init() {
        produto = new Produto();
        produto.setCodigo("P001");
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição do Produto Teste");
        produto.setValor(BigDecimal.valueOf(50.0));
        produto.setQuantidadeEstoque(10);
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataAtualizacao(LocalDateTime.now());
    }

    @After
    public void tearDown() throws ExceptionDao {
        produtoService.excluir(produto.getCodigo());
    }

    @Test
    public void pesquisar() throws ExceptionDao, ExceptionTipoChaveNaoEncontrada, SecurityException, ExceptionElementoNaoConhecido {
        produtoService.cadastrar(produto);
        Produto produtoConsultado = this.produtoService.consultar(produto.getCodigo());
        Assert.assertNotNull(produtoConsultado);
    }

    @Test
    public void salvar() throws ExceptionTipoChaveNaoEncontrada, ExceptionDao {
        Boolean retorno = produtoService.cadastrar(produto);
        Assert.assertTrue(retorno);
    }

    @Test
    public void excluir() throws ExceptionDao, ExceptionTipoChaveNaoEncontrada, SecurityException, ExceptionElementoNaoConhecido {
        produtoService.cadastrar(produto);
        produtoService.excluir(produto.getCodigo());
        Produto produtoConsultado = this.produtoService.consultar(produto.getCodigo());
        Assert.assertNull(produtoConsultado);
    }

    @Test
    public void alterarProduto() throws ExceptionTipoChaveNaoEncontrada, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido {
        produtoService.cadastrar(produto);
        produto.setNome("Produto Teste Alterado");
        produtoService.alterar(produto);
        
        Produto produtoAlterado = this.produtoService.consultar(produto.getCodigo());
        Assert.assertEquals("Produto Teste Alterado", produtoAlterado.getNome());
    }
}
