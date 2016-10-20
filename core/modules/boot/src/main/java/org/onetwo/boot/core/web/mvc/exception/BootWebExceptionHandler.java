package org.onetwo.boot.core.web.mvc.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.data.Result;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BootWebExceptionHandler {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@ExceptionHandler
	public Object doResolveException(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws Throwable {
		Result<?, ?> result = BootWebUtils.webHelper(request).getAjaxErrorResult();
		if(result!=null){
			ResponseEntity<?> reponseEntity = new ResponseEntity<>(result, HttpStatus.OK);
			return reponseEntity;
		}
		
		logger.error("unhandle Throwable: ", ex.getMessage());
		throw ex;
	}
}
