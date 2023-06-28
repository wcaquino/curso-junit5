package br.ce.wcaquino.barriga.service;

import java.time.LocalDateTime;

import br.ce.wcaquino.barriga.domain.Transacao;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.repositories.TransacaoDao;

public class TransacaoService {
	private TransacaoDao dao;

	public Transacao salvar(Transacao transacao) {
		if(getTime().getHour() > 10)
			throw new RuntimeException("Tente novamente amanhã");
		
		validarCamposObrigatorios(transacao);
		
		return dao.salvar(transacao);
	}

	private void validarCamposObrigatorios(Transacao transacao) {
		if(transacao.getDescricao() == null) throw new ValidationException("Descrição inexistente");
		if(transacao.getValor() == null) throw new ValidationException("Valor inexistente");
		if(transacao.getData() == null) throw new ValidationException("Data inexistente");
		if(transacao.getConta() == null) throw new ValidationException("Conta inexistente");
		if(transacao.getStatus() == null) transacao.setStatus(false);
	}
	
	protected LocalDateTime getTime() {
		return LocalDateTime.now();
	}
}
