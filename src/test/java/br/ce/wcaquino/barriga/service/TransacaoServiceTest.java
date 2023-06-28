package br.ce.wcaquino.barriga.service;

import static br.ce.wcaquino.barriga.domain.builders.ContaBuilder.umaConta;
import static br.ce.wcaquino.barriga.domain.builders.TransacaoBuilder.umaTransacao;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import br.ce.wcaquino.barriga.domain.Conta;
import br.ce.wcaquino.barriga.domain.Transacao;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.external.ClockService;
import br.ce.wcaquino.barriga.service.repositories.TransacaoDao;

@Tag("service")
@Tag("transacao")
//@EnabledIf(value = "isHoraValida")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransacaoServiceTest {
	@InjectMocks @Spy private TransacaoService service;
	@Mock private TransacaoDao dao;
	
	@Captor private ArgumentCaptor<Transacao> captor;
	
	@BeforeEach
	public void setup() {
//		when(clock.getCurrentTime()).thenReturn(LocalDateTime.of(2023,1,1,4,30,15));		
		when(service.getTime()).thenReturn(LocalDateTime.of(2023,1,1,4,30,15));
	}

	@Test
	public void deveSalvarTransacaoValida() {
		Transacao transacaoParaSalvar = umaTransacao().comId(null).agora();
		when(dao.salvar(transacaoParaSalvar)).thenReturn(umaTransacao().agora());
		
		Transacao transacaoSalva = service.salvar(transacaoParaSalvar);
		Assertions.assertEquals(umaTransacao().agora(), transacaoSalva);
		Assertions.assertAll("Transacao", 
			() -> assertEquals(1L, transacaoSalva.getId()),
			() -> assertEquals("Transação Válida", transacaoSalva.getDescricao()),
			() -> {
				assertAll("Conta",
					() -> assertEquals("Conta Válida", transacaoSalva.getConta().nome()),
					() -> {
						assertAll("Usuário",
							() -> assertEquals("Usuário Válido", transacaoSalva.getConta().usuario().nome()),
							() -> assertEquals("12345678", transacaoSalva.getConta().usuario().senha())
						);
					}
				);
			}
		);
	}
	
	@ParameterizedTest(name = "{6}")
	@MethodSource(value = "cenariosObrigatorios")
	public void deveValidarCamposObrigatoriosAoSalvar(Long id, String descricao, Double valor, 
											LocalDate data, Conta conta, Boolean status, String mensagem) {
		String exMessage = Assertions.assertThrows(ValidationException.class, () -> {
			Transacao transacao = umaTransacao().comId(id).comDescricao(descricao).comValor(valor)
					.comData(data).comStatus(status).comConta(conta).agora();
			service.salvar(transacao);
		}).getMessage();
		Assertions.assertEquals(mensagem, exMessage);
	}
	
	static Stream<Arguments> cenariosObrigatorios() {
		return Stream.of(
			Arguments.of(1L, null, 10D, LocalDate.now(), umaConta().agora(), true, "Descrição inexistente"),
			Arguments.of(1L, "Descrição", null, LocalDate.now(), umaConta().agora(), true, "Valor inexistente"),
			Arguments.of(1L, "Descrição", 10D, null, umaConta().agora(), true, "Data inexistente"),
			Arguments.of(1L, "Descrição", 10D, LocalDate.now(), null, true, "Conta inexistente")
		);
	}
	
	@Test
	public void deveRejeitarTransacaoSemValor() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Transacao transacao = umaTransacao().comValor(null).agora();
		
		Method metodo = TransacaoService.class.getDeclaredMethod("validarCamposObrigatorios", Transacao.class);
		metodo.setAccessible(true);
		Exception ex = Assertions.assertThrows(Exception.class, () -> 
			metodo.invoke(new TransacaoService(), transacao));
		Assertions.assertEquals("Valor inexistente", ex.getCause().getMessage());
	}
	
	@Test
	public void deveRejeitarTransacaoTardeDaNoite() {
//		Mockito.reset(service);
		when(service.getTime()).thenReturn(LocalDateTime.of(2023,1,1,23,30,15));
		String exMessage = Assertions.assertThrows(RuntimeException.class, () -> {
			service.salvar(umaTransacao().agora());
		}).getMessage();
		Assertions.assertEquals("Tente novamente amanhã", exMessage);
	}
	
	@Test
	public void deveSalvaTransacaoComoPendentePorPadrao() {
		Transacao transacao = umaTransacao().comStatus(null).agora();
		service.salvar(transacao);
		
		Mockito.verify(dao).salvar(captor.capture());
		Transacao transacaoValidada = captor.getValue();
		Assertions.assertFalse(transacaoValidada.getStatus());
	}
	
//	public static boolean isHoraValida() {
//		return LocalDateTime.now().getHour() > 17;
//	}
}
