package org.onetwo.ext.security.exception;

import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class AccessDeniedCodeException extends AccessDeniedException implements ExceptionCodeMark {
	
	private ErrorType errorType;
	private Object[] args;

	public AccessDeniedCodeException(ErrorType errorType) {
		super(errorType.getErrorMessage());
		this.errorType = errorType;
	}

	@Override
	public Object[] getArgs() {
		return args;
	}

	@Override
	public String getCode() {
		return errorType.getErrorCode();
	}
	
}
