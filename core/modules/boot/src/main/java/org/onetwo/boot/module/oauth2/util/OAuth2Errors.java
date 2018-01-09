package org.onetwo.boot.module.oauth2.util;

import java.util.stream.Stream;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public enum OAuth2Errors implements ErrorType {
	CLIENT_ACCESS_TOKEN_NOT_FOUND("access_token参数错误！"),
	CLIENT_ACCESS_TOKEN_INVALID("access_token无效！");

	final private String message;

	private OAuth2Errors(String message) {
		this.message = message;
	}

	@Override
	public String getErrorCode() {
		return name();
	}

	public String getErrorMessage() {
		return message;
	}
	public static OAuth2Errors of(String status){
		return Stream.of(values()).filter(s->s.name().equalsIgnoreCase(status))
									.findAny()
									.orElseThrow(()->new IllegalArgumentException("error status: " + status));
	}
	
}
