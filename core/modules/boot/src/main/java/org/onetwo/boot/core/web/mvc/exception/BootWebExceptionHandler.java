package org.onetwo.boot.core.web.mvc.exception;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class BootWebExceptionHandler extends ResponseEntityExceptionHandler {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@ExceptionHandler({
		Throwable.class
	})
	public ResponseEntity<Object> doResolveException(Exception ex, WebRequest request) {
		return super.handleException(ex, request);
	}
	
}
