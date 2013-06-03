package org.onetwo.common.ejb.exception;

import javax.ejb.ApplicationException;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode;

@SuppressWarnings("serial")
@ApplicationException(rollback=true)
public class AppEJBException extends ServiceException implements SystemErrorCode {
	
	protected String code = ServiceErrorCode.BASE_CODE;

	public AppEJBException() { 
		super();
	}

	public AppEJBException(Exception ex) {
		super(ex);
	}

	public AppEJBException(String message, Exception ex) {
		super(message, ex);
	}

	public AppEJBException(String message) {
		super(message);
	}

}
