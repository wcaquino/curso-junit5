package br.ce.wcaquino.barriga.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Transacao {
	private Long id;
	private String descricao;
	private Double valor;
	private Conta conta;
	private LocalDate data;
	private Boolean status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Conta getConta() {
		return conta;
	}
	public void setConta(Conta conta) {
		this.conta = conta;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(conta, data, descricao, status, valor);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transacao other = (Transacao) obj;
		return Objects.equals(conta, other.conta) && Objects.equals(data, other.data)
				&& Objects.equals(descricao, other.descricao) && Objects.equals(status, other.status)
				&& Objects.equals(valor, other.valor);
	}
}
