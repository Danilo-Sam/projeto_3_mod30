package test.br.com.dsampaio;

import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionElementoNaoConhecido;
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

    @Before
    public void init() throws ExceptionDao {
        IClienteDAO dao = new ClienteDAO();
        clienteService = new ClienteService(dao);
        
        cliente = new Cliente();
        cliente.setCpf(12345678901L);
        cliente.setNome("João da Silva");
        cliente.setCidade("São Paulo");
        cliente.setEndereco("Rua A");
        cliente.setEstado("SP");
        cliente.setNumero(100);
        cliente.setTelefone(11999999999L);
        cliente.setIdade(30);
        cliente.setEmail("joao.silva@example.com");

        // Garante que o cliente não exista antes do teste
        clienteService.excluir(cliente.getCpf());
    }

    @After
    public void tearDown() throws ExceptionDao {
        // Remove o cliente após cada teste
        clienteService.excluir(cliente.getCpf());
    }

    @Test
    public void pesquisarCliente() throws ExceptionDao, ExceptionTipoChaveNaoEncontrada, SecurityException, ExceptionElementoNaoConhecido {
        clienteService.cadastrar(cliente);
        Cliente clienteConsultado = clienteService.consultar(cliente.getCpf());
        Assert.assertNotNull("O cliente deveria ser encontrado", clienteConsultado);
        Assert.assertEquals("O CPF do cliente consultado não corresponde", cliente.getCpf(), clienteConsultado.getCpf());
    }

    @Test
    public void salvarCliente() throws ExceptionTipoChaveNaoEncontrada, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido {
        Boolean retorno = clienteService.cadastrar(cliente);
        Assert.assertTrue("O cliente deveria ser cadastrado com sucesso", retorno);
        
        Cliente clienteConsultado = clienteService.consultar(cliente.getCpf());
        Assert.assertNotNull("O cliente deveria ser encontrado após o cadastro", clienteConsultado);
    }

    @Test
    public void excluirCliente() throws ExceptionDao, ExceptionTipoChaveNaoEncontrada, SecurityException, ExceptionElementoNaoConhecido {
        clienteService.cadastrar(cliente);
        clienteService.excluir(cliente.getCpf());
        Cliente clienteConsultado = clienteService.consultar(cliente.getCpf());
        Assert.assertNull("O cliente deveria ser excluído", clienteConsultado);
    }

    @Test
    public void alterarCliente() throws ExceptionTipoChaveNaoEncontrada, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido {
        clienteService.cadastrar(cliente);
        cliente.setNome("João da Silva Jr.");
        clienteService.alterar(cliente);
        Cliente clienteAlterado = clienteService.consultar(cliente.getCpf());
        Assert.assertNotNull("O cliente deveria ser encontrado após a alteração", clienteAlterado);
        Assert.assertEquals("O nome do cliente alterado não corresponde", "João da Silva Jr.", clienteAlterado.getNome());
    }
}
