package br.com.dsampaio.dao.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.dsampaio.dao.Persistente;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionMaisDeUmRegistro;
import br.com.dsampaio.exceptions.ExceptionTable;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;

public interface IGenericDAO<T extends Persistente, E extends Serializable> {

    public Boolean cadastrar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao;

    public void excluir(E valor) throws ExceptionDao;

    public void alterar(T entity) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao;

    public T consultar(E valor) throws ExceptionMaisDeUmRegistro, ExceptionTable, ExceptionDao;

    public Collection<T> buscarTodos() throws ExceptionDao;
}
