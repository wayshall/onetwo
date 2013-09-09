package org.onetwo.common.fish.exception;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.utils.Assert;

/***********
 * 
 * 业务异常
 * 业务方法应抛出此业务
 * 
 * 
 * @author wayshall
 *
 */
public class JFishServiceException extends BaseException implements SystemErrorCode, ExceptionCodeMark, ExceptionMessageArgs {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4065494389003261787L;

	public static JFishServiceException create(String code, Object...args){
		Assert.hasText(code);
		JFishServiceException e = new JFishServiceException(code);
		e.setArgs(args);
		return e;
	}

	public static JFishServiceException createByMsg(String msg){
		JFishServiceException e = new JFishServiceException(msg, null);
		return e;
	}
	
	public static final String DEFAULT_MESSAGE = "[service error 业务错误]:";

	private Object[] args;

	public JFishServiceException(String code, Throwable cause, String msg) {
		super(msg, cause);
		initErrorCode(code);
	}

	public JFishServiceException(String code) {
		initErrorCode(code);
	}

	public JFishServiceException(String code, Throwable cause) {
		this(DEFAULT_MESSAGE, cause, code);
	}

	public String getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
}
