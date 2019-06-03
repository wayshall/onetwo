package org.onetwo.ext.ons.exception;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorType;

/****
 * 一般消息数据不符合业务逻辑，不可能消费都时候抛出。
 * 异常会被log记录，但会返回成功，不再重试消费
 * @author way
 *
 */
@SuppressWarnings("serial")
public class ImpossibleConsumeException extends BaseException{

	public ImpossibleConsumeException() {
		super("message impossible consume");
	}


	public ImpossibleConsumeException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType.getErrorMessage(), cause, exceptionType.getErrorCode());
	}
	
	public ImpossibleConsumeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ImpossibleConsumeException(String msg) {
		super(msg);
	}

	
}
