package org.onetwo.common.exception;

import org.onetwo.common.utils.StringUtils;

/*********
 * 
 * @author wayshall
 *
 */
//@Deprecated
public class ServiceException extends BaseException implements ExceptionCodeMark{

	public static ServiceException formatMessage(String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new ServiceException(formatMsg);
	}
	public static ServiceException formatMessage(Throwable cause, String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new ServiceException(formatMsg, cause);
	}

	public static ServiceException formatCodeMessage(String code, Object...args){
		return formatCodeMessage(null, code, args);
	}
	public static ServiceException formatCodeMessage(Throwable cause, String code, Object...args){
		ServiceException se = new ServiceException(cause, code);
		se.setArgs(args);
		return se;
	}

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

	public ServiceException(ErrorType exceptionType) {
		this(exceptionType, null);
	}

	public ServiceException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType.getErrorMessage(), cause);
		initErrorCode(exceptionType.getErrorCode());
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
		return ServiceErrorCode.BASE_CODE;
	}

	public boolean isDefaultErrorCode(){
		return ServiceErrorCode.BASE_CODE.equals(getCode());
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
