package org.onetwo.common.exception;


public class NotLoginException extends AuthenticationException {
	public static final String DEFAULT_MESSAGE = "请先登录！";
	public static final String DEFAULT_CODE = "CM_NOT_LOGIN";

	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public NotLoginException() {
		super(DEFAULT_MESSAGE, DEFAULT_CODE);
	}

	public NotLoginException(String message, String code) {
		super(message, code);
	}

	public NotLoginException(String message) {
		super(message);
	}

	public NotLoginException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public NotLoginException(String message, Throwable cause) {
		super(message, cause);
	}
	public NotLoginException(ErrorType exceptionType) {
		super(exceptionType);
	}

}
