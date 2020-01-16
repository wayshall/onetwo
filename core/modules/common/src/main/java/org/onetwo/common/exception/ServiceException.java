package org.onetwo.common.exception;

import java.util.Map;
import java.util.Optional;

import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Maps;

/*********
 * 
 * @author wayshall
 *
 */
//@Deprecated
public class ServiceException extends BaseException implements ExceptionCodeMark, HeaderableException {

	public static ServiceException formatMessage(String msg, Object...args){
		String formatMsg = String.format(msg, args);
		ServiceException se =  new ServiceException(formatMsg);
		se.setArgs(args);
		return se;
	}
	public static ServiceException formatMessage(Throwable cause, String msg, Object...args){
		String formatMsg = String.format(msg, args);
		ServiceException se = new ServiceException(formatMsg, cause);
		se.setArgs(args);
		return se;
	}

	public static ServiceException formatMessage(ErrorType exceptionType, Object...args){
		String formatMsg = String.format(exceptionType.getErrorMessage(), args);
		ServiceException se = new ServiceException(formatMsg, exceptionType.getErrorCode());
		se.exceptionType = exceptionType;
		se.setArgs(args);
		return se;
	}


	/**
	 *  
	 */
	private static final long serialVersionUID = 7280411050853219784L;
	
//	protected String code;
	private Object[] args;
	private Integer statusCode;
	private ErrorType exceptionType;
	
	private Map<String, String> headers;

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
		this(exceptionType, cause, null);
	}
	
	public ServiceException(ErrorType exceptionType, Throwable cause, String message) {
		super(exceptionType.getErrorMessage() + StringUtils.emptyIfNull(message), cause);
		initErrorCode(exceptionType.getErrorCode());
		this.statusCode = exceptionType.getStatusCode();
		this.exceptionType = exceptionType;
	}
	
	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ServiceException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public ErrorType getExceptionType() {
		return exceptionType;
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
	@Override
	public String getCode() {
		return code;
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
	
	public Optional<Integer> getStatusCode() {
		return Optional.ofNullable(statusCode);
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
	final protected Map<String, String> headers() {
		Map<String, String> headers = this.headers;
		if(headers==null){
			headers = Maps.newHashMap();
			this.headers = headers;
		}
		return headers;
	}
	
	public Optional<Map<String, String>> getHeaders() {
		return Optional.ofNullable(headers);
	}
	
	@SuppressWarnings("unchecked")
	final public <T extends BaseException> T header(String key, String value){
		headers().put(key, value);
		return (T)this;
	}

}
