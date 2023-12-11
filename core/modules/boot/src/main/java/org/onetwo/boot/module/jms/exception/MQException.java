package org.onetwo.boot.module.jms.exception;

import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.exception.ServiceException;

@SuppressWarnings("serial")
public class MQException extends ServiceException  {

	public MQException(String msg) {
		super(msg);
	}

	public MQException(String msg, String code) {
		super(msg, code);
	}

	public MQException(ErrorType exceptionType) {
		super(exceptionType);
	}


	public MQException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType, cause, null);
	}
	
	public MQException(String msg, Throwable cause) {
		super(msg, cause);
	}
	

}
