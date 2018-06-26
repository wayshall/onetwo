package org.onetwo.boot.module.redis;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public enum TokenValidatorErrors implements ErrorType {
	REQUIRED_VALUE("the token is reqired"),
	TOKEN_INVALID("invalid token")
	;

	final private String errorMessage;
	
	private TokenValidatorErrors(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getErrorCode() {
		return name();
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
