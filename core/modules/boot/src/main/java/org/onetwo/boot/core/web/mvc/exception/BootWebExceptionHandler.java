package org.onetwo.boot.core.web.mvc.exception;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/***
 * TODO 
 * 
 * @ExceptionHandler 声明的异常， 深度越接近实际异常的方法优先匹配
 * @author way
 *
 */
//@ControllerAdvice
public class BootWebExceptionHandler extends ResponseEntityExceptionHandler implements ExceptionMessageFinder {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Autowired
	private BootSiteConfig bootSiteConfig;
	@Autowired(required=false)
	private ExceptionMessageAccessor exceptionMessageAccessor;
	
	
	@ExceptionHandler({
		Throwable.class
	})
	public ResponseEntity<Object> handleUnhandledException(Exception ex, WebRequest request) {
//		return super.handleException(ex, request);
		HttpHeaders headers = new HttpHeaders();
		logger.warn("Unknown exception type: " + ex.getClass().getName());
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorMessage errorMessage = this.getErrorMessage(ex, bootSiteConfig.isProduct());
		//TODO
		
		return super.handleExceptionInternal(ex, null, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}


	@Override
	public ExceptionMessageAccessor getExceptionMessageAccessor() {
		return exceptionMessageAccessor;
	}
	
	
}
