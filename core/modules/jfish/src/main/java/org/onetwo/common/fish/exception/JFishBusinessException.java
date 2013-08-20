package org.onetwo.common.fish.exception;

import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.utils.Assert;

/***********
 * 
 * 重新定义的业务异常
 * 业务方法应抛出此业务
 * 
 * 需要抛错而又不想回滚事务时，可抛出此异常
 * 
 * 注意，如果想回滚事务，请抛出{@link org.onetwo.common.fish.exception.JFishServiceException}，或者在事务声明中指定JFishBusinessException要回滚。
 * @author wayshall
 *
 */
public class JFishBusinessException extends Exception implements SystemErrorCode, ExceptionCodeMark, ExceptionMessageArgs {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4065494389003261787L;

	public static JFishBusinessException create(String code, Object...args){
		Assert.hasText(code);
		JFishBusinessException e = new JFishBusinessException(code);
		e.setArgs(args);
		return e;
	}
	
	public static final String DEFAULT_MESSAGE = "[business error 业务错误]:";

	private String code = BusinessErrorCode.BASE_CODE;
	private Object[] args;

	public JFishBusinessException(String msg, Throwable cause, String code) {
		super(msg, cause);
		this.code = code;
	}

	public JFishBusinessException(String code) {
		this.code = code;
	}

	public JFishBusinessException(String code, Throwable cause) {
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
