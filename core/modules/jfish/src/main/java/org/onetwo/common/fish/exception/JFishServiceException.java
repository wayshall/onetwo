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
	
	public static final String DEFAULT_MESSAGE = "[service error 业务错误]:";

	private String code = BusinessErrorCode.BASE_CODE;
	private Object[] args;

	public JFishServiceException(String msg, Throwable cause, String code) {
		super(msg, cause);
		this.code = code;
	}

	public JFishServiceException(String code) {
		this.code = code;
	}

	public JFishServiceException(String code, Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		this.code = code;
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
