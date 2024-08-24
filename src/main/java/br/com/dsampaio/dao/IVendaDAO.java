package br.com.dsampaio.dao;

import br.com.dsampaio.dao.generic.IGenericDAO;
import br.com.dsampaio.domain.Venda;
import br.com.dsampaio.exceptions.ExceptionDao;
import br.com.dsampaio.exceptions.ExceptionTipoChaveNaoEncontrada;

public interface IVendaDAO extends IGenericDAO<Venda, String> {
    
    void finalizarVenda(Venda venda) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao;
    
    void cancelarVenda(Venda venda) throws ExceptionTipoChaveNaoEncontrada, ExceptionDao;
}
