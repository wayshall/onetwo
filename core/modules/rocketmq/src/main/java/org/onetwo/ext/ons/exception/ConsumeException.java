package org.onetwo.ext.ons.exception;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorType;

@SuppressWarnings("serial")
public class ConsumeException extends BaseException{
	
	public ConsumeException() {
		super("consume message error");
	}


	public ConsumeException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType.getErrorMessage(), cause, exceptionType.getErrorCode());
	}
	
	public ConsumeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ConsumeException(String msg) {
		super(msg);
	}

	
}
