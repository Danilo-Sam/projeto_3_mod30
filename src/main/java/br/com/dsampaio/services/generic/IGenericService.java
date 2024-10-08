package br.com.dsampaio.services.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.dsampaio.dao.Persistente;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionElementoNaoConhecido;
import br.com.dsampaio.exceptions.ExceptionTable;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;

public interface IGenericService<T extends Persistente, E extends Serializable> {

    public Boolean cadastrar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao;

    public void alterar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao;

    public void excluir(E valor) throws ExceptionDao;

    public T consultar(E valor) throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTipoChaveNaoEncontrada;

    public Collection<T> buscarTodos() throws ExceptionDao, SecurityException, ExceptionElementoNaoConhecido, ExceptionTable;
}
