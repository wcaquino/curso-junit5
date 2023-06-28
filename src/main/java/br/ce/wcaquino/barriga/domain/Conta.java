package br.ce.wcaquino.barriga.domain;

import java.util.Objects;

import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;

public class Conta {
	private Long id;
	private String nome;
	private Usuario usuario;
	
	public Conta(Long id, String nome, Usuario usuario) {
		if(nome == null) throw new ValidationException("Nome é obrigatório");
		if(usuario == null) throw new ValidationException("Usuário é obrigatório");
		
		this.id = id;
		this.nome = nome;
		this.usuario = usuario;
	}

	public Long id() {
		return id;
	}

	public String nome() {
		return nome;
	}

	public Usuario usuario() {
		return usuario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conta other = (Conta) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "Conta [id=" + id + ", nome=" + nome + ", usuario=" + usuario + "]";
	}
}
