package org.onetwo.boot.core.web.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

public class ExceptionMessageAccessor extends MessageSourceAccessor {
	public static final String BEAN_EXCEPTION_MESSAGE = "exceptionMessage";

	public ExceptionMessageAccessor(MessageSource messageSource) {
		super(messageSource);
	}

}
