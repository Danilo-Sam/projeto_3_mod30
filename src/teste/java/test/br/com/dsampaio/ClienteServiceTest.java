package test.br.com.dsampaio;

import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;
import br.com.dsampaio.services.ClienteService;
import br.com.dsampaio.services.IClienteService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.dsampaio.dao.ClienteDAO;
import br.com.dsampaio.dao.IClienteDAO;
import br.com.dsampaio.domain.Cliente;

public class ClienteServiceTest {
    
    private IClienteService clienteService;
    private Cliente cliente;

    public ClienteServiceTest() {
        IClienteDAO dao = new ClienteDAO(); // Use ClienteDAO real
        clienteService = new ClienteService(dao);
    }

    @Before
    public void init() throws Exception {
        cliente = new Cliente();
        cliente.setCpf(12345678901L);
        cliente.setNome("João da Silva");
        cliente.setCidade("São Paulo");
        cliente.setEndereco("Rua A");
        cliente.setEstado("SP");
        cliente.setNumero(100);
        cliente.setTelefone(11999999999L);
        cliente.setIdade(30L);
        cliente.setEmail("joao.silva@example.com");
        clienteService.excluir(cliente.getCpf());
    }

    @After
    public void tearDown() throws ExceptionDao {
        clienteService.excluir(cliente.getCpf());
    }

    @Test
    public void pesquisarCliente() throws ExceptionDao, ExceptionTipoChaveNaoEncontrada {
        clienteService.cadastrar(cliente);
        Cliente clienteConsultado = clienteService.consultar(cliente.getCpf());
        Assert.assertNotNull(clienteConsultado);
        Assert.assertEquals(cliente.getCpf(), clienteConsultado.getCpf());
    }

    @Test
    public void salvarCliente() throws ExceptionTipoChaveNaoEncontrada, ExceptionDao {
        Boolean retorno = clienteService.cadastrar(cliente);
        Assert.assertTrue(retorno);
    }

    @Test
    public void excluirCliente() throws ExceptionDao, ExceptionTipoChaveNaoEncontrada {
        clienteService.cadastrar(cliente);
        clienteService.excluir(cliente.getCpf());
        Cliente clienteConsultado = clienteService.consultar(cliente.getCpf());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void alterarCliente() throws ExceptionTipoChaveNaoEncontrada, ExceptionDao {
        clienteService.cadastrar(cliente);
        cliente.setNome("João da Silva Jr.");
        clienteService.alterar(cliente);
        Cliente clienteAlterado = clienteService.consultar(cliente.getCpf());
        Assert.assertEquals("João da Silva Jr.", clienteAlterado.getNome());
    }
}
