package org.onetwo.common.exception;

import org.onetwo.common.utils.StringUtils;

/*********
 * 
 * @author wayshall
 *
 */
//@Deprecated
public class ServiceException extends BaseException implements ExceptionCodeMark{

	/**
	 *  
	 */
	private static final long serialVersionUID = 7280411050853219784L;
	
	protected String code;
	private Object[] args;

	public ServiceException() {
		super();
	} 


	public ServiceException(String msg, String code) {
		super(msg);
		initErrorCode(code);
	}

	public ServiceException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}


	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public ServiceException(String msg) {
		super(msg);
	}


	public ServiceException(Throwable cause, String code) {
		super(cause);
		initErrorCode(code);
	}

	
	final protected void initErrorCode(String code){
		if(StringUtils.isNotBlank(code))
			this.code = code;//appendBaseCode(code);
	}
	public String getCode() {
		if(StringUtils.isBlank(code))
			return getDefaultCode();
		return code;
	}
	
	protected String getDefaultCode(){
		return DEFAULT_SYSTEM_ERROR_CODE;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	protected void setErrorCode(){
		this.code = ServiceErrorCode.BASE_CODE;
	}

}
