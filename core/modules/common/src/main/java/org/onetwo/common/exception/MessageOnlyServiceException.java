package org.onetwo.common.exception;
/**
 * 用于一些直接抛出信息提示作为异常，但又无需回滚事务但场景
 * 
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class MessageOnlyServiceException extends ServiceException {

	public MessageOnlyServiceException(String msg) {
		super(msg);
	}

	public MessageOnlyServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MessageOnlyServiceException(String msg, String code) {
		super(msg, code);
	}

	public MessageOnlyServiceException(ErrorType exceptionType) {
		super(exceptionType, null);
	}
}
