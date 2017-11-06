package org.onetwo.common.exception;


@SuppressWarnings("serial")
public class AuthenticationException extends ServiceException {
	public static final String DEFAULT_MESSAGE = "authentication error ";

	public AuthenticationException() {
		super(DEFAULT_MESSAGE);
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, String code) {
		super(message, code);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
