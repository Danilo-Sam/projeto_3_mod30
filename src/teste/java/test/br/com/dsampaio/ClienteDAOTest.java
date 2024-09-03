package test.br.com.dsampaio;

import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.dsampaio.dao.ClienteDAO;
import br.com.dsampaio.dao.IClienteDAO;
import br.com.dsampaio.domain.Cliente;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionElementoNaoConhecido;
import br.com.dsampaio.exceptions.ExceptionMaisDeUmRegistro;
import br.com.dsampaio.exceptions.ExceptionTable;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;

public class ClienteDAOTest {
    
    private IClienteDAO clienteDao;

    @Before
    public void setUp() {
        clienteDao = new ClienteDAO();
    }

    @After
    public void tearDown() throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTable {
        Collection<Cliente> list = clienteDao.buscarTodos();
        for (Cliente cliente : list) {
            clienteDao.excluir(cliente.getCpf());
        }
    }

    private Cliente criarCliente(Long cpf, String nome) {
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);
        cliente.setNome(nome);
        cliente.setCidade("Cidade Exemplo");
        cliente.setEndereco("Endereco Exemplo");
        cliente.setEstado("EX");
        cliente.setNumero(123);
        cliente.setTelefone(1199999999L);
        cliente.setIdade(30);
        cliente.setEmail(nome.toLowerCase().replace(" ", ".") + "@example.com");
        return cliente;
    }

    @Test
    public void testPesquisarCliente() throws ExceptionMaisDeUmRegistro, ExceptionTable, ExceptionTipoChaveNaoEncontrada, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido {
        Cliente cliente = criarCliente(11111111111L, "Carlos Alberto");
        clienteDao.cadastrar(cliente);
        
        Cliente clienteConsultado = clienteDao.consultar(cliente.getCpf());
        Assert.assertNotNull("Cliente não encontrado", clienteConsultado);
        Assert.assertEquals("Nome do cliente não corresponde", "Carlos Alberto", clienteConsultado.getNome());
    }
    
    @Test
    public void testSalvarCliente() throws ExceptionTipoChaveNaoEncontrada, ExceptionMaisDeUmRegistro, ExceptionTable, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido {
        Cliente cliente = criarCliente(22222222222L, "Patrícia Lima");
        Boolean retorno = clienteDao.cadastrar(cliente);
        Assert.assertTrue("Cliente não foi cadastrado", retorno);
        
        Cliente clienteConsultado = clienteDao.consultar(cliente.getCpf());
        Assert.assertNotNull("Cliente não encontrado", clienteConsultado);
        Assert.assertEquals("Nome do cliente não corresponde", "Patrícia Lima", clienteConsultado.getNome());
    }
    
    @Test
    public void testExcluirCliente() throws ExceptionTipoChaveNaoEncontrada, ExceptionMaisDeUmRegistro, ExceptionTable, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido {
        Cliente cliente = criarCliente(33333333333L, "Joana Santos");
        clienteDao.cadastrar(cliente);
        
        Cliente clienteConsultado = clienteDao.consultar(cliente.getCpf());
        Assert.assertNotNull("Cliente não encontrado antes da exclusão", clienteConsultado);
        
        clienteDao.excluir(cliente.getCpf());
        clienteConsultado = clienteDao.consultar(cliente.getCpf());
        Assert.assertNull("Cliente encontrado após a exclusão", clienteConsultado);
    }
    
    @Test
    public void testAlterarCliente() throws ExceptionTipoChaveNaoEncontrada, ExceptionMaisDeUmRegistro, ExceptionTable, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido {
        Cliente cliente = criarCliente(44444444444L, "Fernando Costa");
        clienteDao.cadastrar(cliente);
        
        Cliente clienteConsultado = clienteDao.consultar(cliente.getCpf());
        Assert.assertNotNull("Cliente não encontrado antes da alteração", clienteConsultado);
        
        clienteConsultado.setNome("Fernando da Costa");
        clienteDao.alterar(clienteConsultado);
        
        Cliente clienteAlterado = clienteDao.consultar(clienteConsultado.getCpf());
        Assert.assertNotNull("Cliente não encontrado após a alteração", clienteAlterado);
        Assert.assertEquals("Nome do cliente não foi alterado", "Fernando da Costa", clienteAlterado.getNome());
    }
    
    @Test
    public void testBuscarTodos() throws ExceptionTipoChaveNaoEncontrada, ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTable {
        Cliente cliente1 = criarCliente(55555555555L, "Gabriela Souza");
        Cliente cliente2 = criarCliente(66666666666L, "Renato Fernandes");
        
        clienteDao.cadastrar(cliente1);
        clienteDao.cadastrar(cliente2);
        
        Collection<Cliente> list = clienteDao.buscarTodos();
        Assert.assertNotNull("A lista de clientes retornada é nula", list);
        Assert.assertEquals("Número incorreto de clientes retornados", 2, list.size());
    }
}

