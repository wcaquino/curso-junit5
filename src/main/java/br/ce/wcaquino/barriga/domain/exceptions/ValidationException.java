package br.ce.wcaquino.barriga.domain.exceptions;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = -2738361854550222002L;

	public ValidationException(String message) {
		super(message);
	}
}
