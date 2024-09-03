package br.com.dsampaio.services;

import br.com.dsampaio.dao.IClienteDAO;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionElementoNaoConhecido;
import br.com.dsampaio.exceptions.ExceptionMaisDeUmRegistro;
import br.com.dsampaio.exceptions.ExceptionTable;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;
import br.com.dsampaio.services.generic.GenericService;
import br.com.dsampaio.domain.Cliente;

public class ClienteService extends GenericService<Cliente, Long> implements IClienteService{
	
	public ClienteService(IClienteDAO clienteDAO) {
		super(clienteDAO);
		//this.clienteDAO = clienteDAO;
	}
	
	public Cliente buscarPorCPF(Long cpf) throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTipoChaveNaoEncontrada {
		try {
			return this.dao.consultar(cpf);
		} catch (ExceptionMaisDeUmRegistro | ExceptionTable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
