package org.onetwo.boot.module.redis;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public enum RedisErrors implements ErrorType {
	TOKEN_INVALID("invalid token")
	;

	final private String errorMessage;
	
	private RedisErrors(String errorMessage) {
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
