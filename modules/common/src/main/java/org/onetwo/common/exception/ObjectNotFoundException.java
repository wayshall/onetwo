package org.onetwo.common.exception;


/***********
 * 
 * 重新定义的业务异常
 * 业务方法应抛出此业务
 * 异常代码参考{@link SystemErrorCode}定义
 * 
 * @author wayshall
 *
 */
public class ObjectNotFoundException extends BusinessException implements SystemErrorCode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5608395998334544038L;

	public static final String DEFAULT_MESSAGE = "[object not found 找不到数据]:";


	public ObjectNotFoundException() {
		super(DEFAULT_MESSAGE);
	}
	
	public ObjectNotFoundException(String msg, String code) {
		super(msg);
		this.initErrorCode(code);
	}


	public ObjectNotFoundException(String msg, Throwable cause, String code) {
		super(msg, cause);
		this.initErrorCode(code);
	}


	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
		this.initErrorCode(null);
	}


	public ObjectNotFoundException(String msg) {
		super(msg);
		this.initErrorCode(null);
	}


	public ObjectNotFoundException(Throwable cause, String code) {
		super(DEFAULT_MESSAGE, cause);
		this.initErrorCode(code);
	}


	public ObjectNotFoundException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		this.initErrorCode(null);
	}
	
	protected String getBaseCode(){
		return BusinessErrorCode.OBJECT_NOT_FOUND;
	}

}
