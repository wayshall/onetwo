package org.onetwo.ext.ons.exception;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorType;

@SuppressWarnings("serial")
public class MessageConsumedException extends BaseException{

	public MessageConsumedException() {
		super("message has consumed");
	}


	public MessageConsumedException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType.getErrorMessage(), cause, exceptionType.getErrorCode());
	}
	
	public MessageConsumedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MessageConsumedException(String msg) {
		super(msg);
	}

	
}
