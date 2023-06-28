import static br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder.umUsuario;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder;
import br.ce.wcaquino.barriga.service.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class CalculadoraMockTest {
	@Spy private Calculadora calc;
	@Mock private UsuarioRepository repo;
	
	@Test
	public void test() {
		when(calc.soma(Mockito.anyInt(), Mockito.eq(2)))
			.thenReturn(5)
			.thenReturn(6)
			.thenCallRealMethod();
		when(repo.salvar(Mockito.any()))
			.thenReturn(umUsuario().agora());
		System.out.println(calc.soma(1, 1));
		System.out.println(calc.soma(1, 2));
		System.out.println(calc.soma(10, 2));
		System.out.println(calc.soma(14, 2));
		System.out.println(calc.soma(167, 2));
		System.out.println(repo.salvar(null));
		System.out.println(repo.salvar(null));
	}
}
