package br.com.dsampaio.services;

import br.com.dsampaio.dao.IProdutoDAO;
import br.com.dsampaio.domain.Produto;
import br.com.dsampaio.services.generic.GenericService;


public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {
	
	public ProdutoService(IProdutoDAO dao) {
		super(dao);
	}
}
