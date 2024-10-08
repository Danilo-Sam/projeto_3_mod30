package br.com.dsampaio.services.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.dsampaio.dao.Persistente;
import br.com.dsampaio.dao.generic.IGenericDAO;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionElementoNaoConhecido;
import br.com.dsampaio.exceptions.ExceptionMaisDeUmRegistro;
import br.com.dsampaio.exceptions.ExceptionTable;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;

public abstract class GenericService<T extends Persistente, E extends Serializable> implements IGenericService<T, E>  {

	protected IGenericDAO<T, E> dao;
	
	
	public GenericService(IGenericDAO<T, E> dao) {
		this.dao = dao;
	}
	
	public Boolean cadastrar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao{
		return this.dao.cadastrar(entity);
	}
	
	public void excluir(E valor) throws ExceptionDao{
		this.dao.excluir(valor);
	}
	
	public void alterar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao {
		this.dao.alterar(entity);
	}
	public T consultar(E valor) throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTipoChaveNaoEncontrada {
		try {
			return this.dao.consultar(valor);
		} catch (ExceptionMaisDeUmRegistro | ExceptionTable e) {
			// TODO Auto-generated catch block
			//TODO levantar um erro genérico
			e.printStackTrace();
		}
		return null;
	}
	public Collection<T> buscarTodos() throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTable {
		return this.dao.buscarTodos();
	}
	
}
