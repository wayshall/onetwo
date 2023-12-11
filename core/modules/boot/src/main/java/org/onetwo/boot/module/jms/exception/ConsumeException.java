package org.onetwo.boot.module.jms.exception;

import org.onetwo.common.exception.ErrorType;

@SuppressWarnings("serial")
public class ConsumeException extends MQException {
	
	public ConsumeException() {
		super("consume message error");
	}


	public ConsumeException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType, cause);
	}
	
	public ConsumeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ConsumeException(String msg) {
		super(msg);
	}

}
