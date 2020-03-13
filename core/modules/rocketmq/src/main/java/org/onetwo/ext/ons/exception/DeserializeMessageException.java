package org.onetwo.ext.ons.exception;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorType;

@SuppressWarnings("serial")
public class DeserializeMessageException extends BaseException{

	public DeserializeMessageException() {
		super("consume message error");
	}


	public DeserializeMessageException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType.getErrorMessage(), cause, exceptionType.getErrorCode());
	}
	
	public DeserializeMessageException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DeserializeMessageException(String msg) {
		super(msg);
	}

	
}
