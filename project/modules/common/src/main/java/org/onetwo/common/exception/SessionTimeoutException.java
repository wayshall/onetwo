package org.onetwo.common.exception;

public class SessionTimeoutException extends NotLoginException {
	public static final String DEFAULT_MESSAGE = "[登录超时]";

	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public SessionTimeoutException() {
		super(DEFAULT_MESSAGE);
	}

	public SessionTimeoutException(String message, String code) {
		super(DEFAULT_MESSAGE+message, code);
	}

	public SessionTimeoutException(String message) {
		super(DEFAULT_MESSAGE+message);
	}

	public SessionTimeoutException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public SessionTimeoutException(String message, Throwable cause) {
		super(DEFAULT_MESSAGE+message, cause);
	}

	@Override
	protected String getBaseCode() {
		return AuthenticErrorCode.SESSION_TIMEOUT;
	}
}
