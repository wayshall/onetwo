package org.onetwo.common.exception;


public class NotLoginException extends AuthenticationException {
	public static final String DEFAULT_MESSAGE = "[not login yet 没有登录]";

	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public NotLoginException() {
		super(DEFAULT_MESSAGE);
	}

	public NotLoginException(String message, String code) {
		super(DEFAULT_MESSAGE+message, code);
	}

	public NotLoginException(String message) {
		super(DEFAULT_MESSAGE+message);
	}

	public NotLoginException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public NotLoginException(String message, Throwable cause) {
		super(DEFAULT_MESSAGE+message, cause);
	}

	@Override
	protected String getDefaultCode() {
		return AuthenticErrorCode.NOT_LOGIN_YET;
	}
}
