package br.ce.wcaquino.barriga.domain.builders;

import br.ce.wcaquino.barriga.domain.Conta;
import java.lang.Long;
import java.util.Arrays;
import java.time.LocalDate;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.String;
import br.ce.wcaquino.barriga.domain.Transacao;


public class TransacaoBuilder {
	private Transacao elemento;
	private TransacaoBuilder(){}

	public static TransacaoBuilder umaTransacao() {
		TransacaoBuilder builder = new TransacaoBuilder();
		inicializarDadosPadroes(builder);
		return builder;
	}

	public static void inicializarDadosPadroes(TransacaoBuilder builder) {
		builder.elemento = new Transacao();
		Transacao elemento = builder.elemento;

		
		elemento.setId(1L);
		elemento.setDescricao("Transação Válida");
		elemento.setValor(10.0);
		elemento.setConta(ContaBuilder.umaConta().agora());
		elemento.setData(LocalDate.now());
		elemento.setStatus(false);
	}

	public TransacaoBuilder comId(Long param) {
		elemento.setId(param);
		return this;
	}

	public TransacaoBuilder comDescricao(String param) {
		elemento.setDescricao(param);
		return this;
	}

	public TransacaoBuilder comValor(Double param) {
		elemento.setValor(param);
		return this;
	}

	public TransacaoBuilder comConta(Conta param) {
		elemento.setConta(param);
		return this;
	}

	public TransacaoBuilder comData(LocalDate param) {
		elemento.setData(param);
		return this;
	}

	public TransacaoBuilder comStatus(Boolean param) {
		elemento.setStatus(param);
		return this;
	}

	public Transacao agora() {
		return elemento;
	}
}
