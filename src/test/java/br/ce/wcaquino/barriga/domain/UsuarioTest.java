package br.ce.wcaquino.barriga.domain;


import static br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder.umUsuario;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;

@Tag("dominio")
@Tag("usuario")
@DisplayName("Domínio: Usuário")
public class UsuarioTest {
	
	@Test
	@DisplayName("Deve criar um usuário válido")
	public void deveCriarUsuarioValido() {
		Usuario usuario = umUsuario().agora();
		umUsuario().comId(28L).agora();
		System.out.println(usuario);
		Assertions.assertAll("Usuario",
				() -> assertEquals(1L, usuario.id()),
				() -> assertEquals("Usuário Válido", usuario.nome()),
				() -> assertEquals("user@mail.com", usuario.email()),
				() -> assertEquals("12345678", usuario.senha())
		);
	}
	
	@Test
	public void deveRejeitarUsuarioSemNome() {
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> 
			umUsuario().comNome(null).agora());
		assertEquals("Nome é obrigatório", ex.getMessage());
	}
	
	@Test
	public void deveRejeitarUsuarioSemEmail() {
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> 
		umUsuario().comEmail(null).agora());
		assertEquals("Email é obrigatório", ex.getMessage());
	}
	
	@Test
	public void deveRejeitarUsuarioSemSenha() {
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> 
		umUsuario().comSenha(null).agora());
		assertEquals("Senha é obrigatória", ex.getMessage());
	}
	
	@ParameterizedTest(name = "[{index}] - {4}")
	@CsvFileSource(files = "src\\test\\resources\\camposObrigatoriosUsuario.csv", nullValues = "NULL", numLinesToSkip = 1)
//	@ParameterizedTest
//	@CsvFileSource(files = "src\\test\\resources\\camposObrigatoriosUsuario.csv", nullValues = "NULL", useHeadersInDisplayName = true)
	public void deveValidarCamposObrigatorios(Long id, String nome, String email, String senha, String mensagem) {
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> 
		umUsuario().comId(id).comNome(nome).comEmail(email).comSenha(senha).agora());
		assertEquals(mensagem, ex.getMessage());
	}
}
