package org.onetwo.common.exception;

public class NoAuthorizationException extends AuthenticationException {

	public static final String DEFAULT_MESSAGE = "[permission deny 没有权限] ";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public NoAuthorizationException() {
		super(DEFAULT_MESSAGE);
	}

	public NoAuthorizationException(String message) {
		super(DEFAULT_MESSAGE+message);
	}

	public NoAuthorizationException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public NoAuthorizationException(String message, Throwable cause) {
		super(DEFAULT_MESSAGE+message, cause);
	}
	protected String getDefaultCode(){
		return AuthenticErrorCode.PERMISSION_DENY;
	}
}
