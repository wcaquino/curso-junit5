package br.ce.wcaquino.barriga.suite;

import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import br.ce.wcaquino.barriga.domain.UsuarioTest;
import br.ce.wcaquino.barriga.service.UsuarioServiceTest;

//@Suite
@SuiteDisplayName("Suite de Testes")
@SelectPackages(value = {
		"br.ce.wcaquino.barriga.service",
		"br.ce.wcaquino.barriga.domain"
	})
public class SuiteTest {

}
