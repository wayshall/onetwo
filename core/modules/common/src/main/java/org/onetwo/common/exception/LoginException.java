package org.onetwo.common.exception;



@SuppressWarnings("serial")
public class LoginException extends ServiceException {
	public static final String DEFAULT_MESSAGE = "Login fail[登录出错]:";

	public LoginException() {
		super(DEFAULT_MESSAGE);
	}

	public LoginException(String message) {
		super(message);
	}
	public LoginException(ErrorType exceptionType) {
		super(exceptionType);
	}

	public LoginException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

}
