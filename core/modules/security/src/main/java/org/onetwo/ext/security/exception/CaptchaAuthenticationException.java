package org.onetwo.ext.security.exception;

import org.onetwo.common.exception.ExceptionCodeMark;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class CaptchaAuthenticationException extends InternalAuthenticationServiceException implements ExceptionCodeMark {
	public static final String ERROR_CODE = "ERR_CAPTCHA";
	

	public CaptchaAuthenticationException(String message) {
		super(message);
	}

	public CaptchaAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public Object[] getArgs() {
		return null;
	}

	@Override
	public String getCode() {
		return ERROR_CODE;
	}
}
