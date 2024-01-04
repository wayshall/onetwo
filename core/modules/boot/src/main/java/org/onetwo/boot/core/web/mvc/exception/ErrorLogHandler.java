package org.onetwo.boot.core.web.mvc.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.web.mvc.exception.ExceptionMessageFinder.ErrorMessage;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ErrorLogHandler {

	void handle(HttpServletRequest request, ErrorMessage errorMessage);
	
}
