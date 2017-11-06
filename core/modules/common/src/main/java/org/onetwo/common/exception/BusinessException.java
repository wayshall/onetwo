package org.onetwo.common.exception;

import java.util.Optional;

/***********
 * 
 * 重新定义的业务异常
 * 业务方法应抛出此业务
 * 异常代码参考{@link SystemErrorCode}定义
 * 
 * 需要抛错而又不想回滚事务时，可抛出此异常
 * 注意，如果想回滚事务，请抛出{@link org.onetwo.common.exception.ServiceException}，或者在事务声明中指定JFishBusinessException要回滚。
 * 
 * @author wayshall
 *
 */
public class BusinessException extends Exception implements SystemErrorCode, ExceptionCodeMark {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5608395998334544038L;

	public static final String DEFAULT_MESSAGE = "[business error 业务错误]:";

	protected String code;
	private Object[] args;

	public BusinessException(String msg, String code) {
		super(msg);
		this.initErrorCode(code);
	}

	@Override
	public Optional<Integer> getStatusCode() {
		return Optional.empty();
	}

	public BusinessException(String msg, Throwable cause, String code) {
		super(msg, cause);
		this.initErrorCode(code);
	}


	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
		this.initErrorCode(null);
	}


	public BusinessException(String msg) {
		super(msg);
		this.initErrorCode(null);
	}


	public BusinessException(Throwable cause, String code) {
		super(DEFAULT_MESSAGE, cause);
		this.initErrorCode(code);
	}


	public BusinessException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		this.initErrorCode(null);
	}
	
	protected final void initErrorCode(String code){
//		this.code = appendBaseCode(code);
		if(code!=null)
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
