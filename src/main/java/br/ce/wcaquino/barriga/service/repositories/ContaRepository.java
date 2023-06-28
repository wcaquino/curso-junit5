package br.ce.wcaquino.barriga.service.repositories;

import java.util.List;

import br.ce.wcaquino.barriga.domain.Conta;

public interface ContaRepository {

	Conta salvar(Conta conta);
	
	List<Conta> obterContasPorUsuario(Long usuarioId);
	
	void delete(Conta conta);
}
