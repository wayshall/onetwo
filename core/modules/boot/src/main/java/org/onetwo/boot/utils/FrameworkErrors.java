package org.onetwo.boot.utils;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public enum FrameworkErrors implements ErrorType {

	SYSTEM("系统错误");

	final private String message;

	private FrameworkErrors(String message) {
		this.message = message;
	}

	@Override
	public String getErrorCode() {
		return name();
	}

	public String getErrorMessage() {
		return message;
	}

}
