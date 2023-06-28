package br.ce.wcaquino.barriga.service.external;

import br.ce.wcaquino.barriga.domain.Conta;

public interface ContaEvent {
	public enum EventType {CREATED, UPDATED, DELETED}
	
	void dispatch(Conta conta, EventType type) throws Exception;
}
