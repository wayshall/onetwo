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

	public LoginException(String message, String code) {
		super(message, code);
	}

	public LoginException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getDefaultCode() {
		return LoginErrorCode.BASE_CODE;
	}
	
	
}
