package br.ce.wcaquino.barriga.service;

import java.time.LocalDateTime;
import java.util.List;

import br.ce.wcaquino.barriga.domain.Conta;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.external.ContaEvent;
import br.ce.wcaquino.barriga.service.external.ContaEvent.EventType;
import br.ce.wcaquino.barriga.service.repositories.ContaRepository;

public class ContaService {
	private ContaRepository repository;
	private ContaEvent event;

	public ContaService(ContaRepository repository, ContaEvent event) {
		this.repository = repository;
		this.event = event;
	}
	
	public Conta salvar(Conta conta) {
		List<Conta> contas = repository.obterContasPorUsuario(conta.usuario().id());
		contas.stream().forEach(contaExistente -> {
			if(conta.nome().equalsIgnoreCase(contaExistente.nome()))
				throw new ValidationException("Usuário já possui uma conta com este nome");
		});
		Conta novaConta = new Conta(conta.id(), conta.nome() + LocalDateTime.now(), conta.usuario());
		System.out.println(novaConta);
		Conta contaPersistida = repository.salvar(novaConta);
		try {
			event.dispatch(contaPersistida, EventType.CREATED);
		} catch (Exception e) {
			repository.delete(contaPersistida);
			throw new RuntimeException("Falha na criação da conta, tente novamente");
		}
		return contaPersistida;
	}
}
