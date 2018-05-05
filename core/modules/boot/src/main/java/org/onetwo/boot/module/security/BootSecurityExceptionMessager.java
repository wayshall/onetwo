package org.onetwo.boot.module.security;

import org.onetwo.boot.core.web.mvc.exception.ExceptionMessageFinder;
import org.onetwo.ext.security.SecurityExceptionMessager;

/**
 * @author wayshall
 * <br/>
 */
public class BootSecurityExceptionMessager implements SecurityExceptionMessager {

	private ExceptionMessageFinder exceptionMessageFinder;
	

	public BootSecurityExceptionMessager(
			ExceptionMessageFinder exceptionMessageFinder) {
		super();
		this.exceptionMessageFinder = exceptionMessageFinder;
	}

	@Override
	public String findMessageByErrorCode(String errorCode, Object... errorArgs) {
		return exceptionMessageFinder.findMessageByErrorCode(errorCode, errorArgs);
	}

	@Override
	public String findMessageByThrowable(Throwable e, Object... errorArgs) {
		return exceptionMessageFinder.findMessageByThrowable(e, errorArgs);
	}

}
