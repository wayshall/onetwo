package org.onetwo.boot.core.web.mvc;

import org.onetwo.boot.core.web.mvc.exception.ExceptionMessageFinder;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultExceptionMessageFinder implements ExceptionMessageFinder {

	private ExceptionMessageAccessor exceptionMessageAccessor;
	
	
	public DefaultExceptionMessageFinder(
			ExceptionMessageAccessor exceptionMessageAccessor) {
		super();
		this.exceptionMessageAccessor = exceptionMessageAccessor;
	}


	@Override
	public ExceptionMessageAccessor getExceptionMessageAccessor() {
		return exceptionMessageAccessor;
	}
	

}
