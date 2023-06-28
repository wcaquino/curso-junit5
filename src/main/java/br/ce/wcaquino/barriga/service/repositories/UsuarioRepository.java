package br.ce.wcaquino.barriga.service.repositories;

import java.util.Optional;

import br.ce.wcaquino.barriga.domain.Usuario;

public interface UsuarioRepository {
	
	Usuario salvar(Usuario usuario);
	
	Optional<Usuario> getUserByEmail(String email);
}
