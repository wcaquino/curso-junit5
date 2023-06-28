package br.ce.wcaquino.barriga.service;

import java.util.Optional;

import br.ce.wcaquino.barriga.domain.Usuario;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.repositories.UsuarioRepository;

public class UsuarioService {
	
	private UsuarioRepository repository;
	
	public UsuarioService(UsuarioRepository repository) {
		this.repository = repository;
	}

	public Usuario salvar(Usuario usuario) {
		repository.getUserByEmail(usuario.email()).ifPresent(user -> {
			throw new ValidationException(String.format("Usuário %s já cadastrado!", usuario.email()));
		});
		return repository.salvar(usuario);
	}

	public Optional<Usuario> getUserByEmail(String email) {
		return repository.getUserByEmail(email);
	}
}
