package br.com.dsampaio.services;


import br.com.dsampaio.domain.Cliente;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionElementoNaoConhecido;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;
import br.com.dsampaio.services.generic.IGenericService;

public interface IClienteService extends IGenericService<Cliente, Long> {
	
	Cliente buscarPorCPF(Long cpf) throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTipoChaveNaoEncontrada;
	
//	void excluir(Long cpf);
//
//	void alterar(Cliente cliente) throws ExceptionTipoChaveNaoEncontrada;
	
//	Boolean cadastrar(Cliente cliente) throws ExceptionTipoChaveNaoEncontrada;


}
