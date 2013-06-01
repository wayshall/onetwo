package org.onetwo.common.exception;



public class ErrorRoleException extends AuthenticationException {

	public static final String DEFAULT_MESSAGE = "[error role 错误的角色] ";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public ErrorRoleException() {
		super(DEFAULT_MESSAGE);
	}

	public ErrorRoleException(String message) {
		super(DEFAULT_MESSAGE+message);
	}

	public ErrorRoleException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public ErrorRoleException(String message, Throwable cause) {
		super(DEFAULT_MESSAGE+message, cause);
	}

	public ErrorRoleException(String message, String code) {
		super(DEFAULT_MESSAGE+message, code);
	}

	@Override
	protected String getBaseCode() {
		return AuthenticErrorCode.ERROR_ROLE;
	}
}
