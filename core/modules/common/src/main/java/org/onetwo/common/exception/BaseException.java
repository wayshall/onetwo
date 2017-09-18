package org.onetwo.common.exception;

import java.io.Serializable;


@SuppressWarnings("serial")
public class BaseException extends RuntimeException implements SystemErrorCode, Serializable{
	
	public static BaseException formatMessage(String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new BaseException(formatMsg);
	}
	public static BaseException formatMessage(Throwable cause, String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new BaseException(formatMsg, cause);
	}

	protected static final String DefaultMsg = "occur error";
	public static final String Prefix = "[ERROR]:";

	protected String code;

//	protected List<Throwable> list = null;

	public BaseException() {
		super(DefaultMsg);
	}

	public BaseException(String msg) {
		super(Prefix + msg);
	}

	public BaseException(Throwable cause) {
		super(DefaultMsg, cause);
	}

	public BaseException(ErrorType exceptionType) {
		super(exceptionType.getErrorMessage());
		this.code = exceptionType.getErrorCode();
	}

	public BaseException(ErrorType exceptionType, Throwable cause) {
		this(exceptionType.getErrorMessage(), cause, exceptionType.getErrorCode());
	}

	public BaseException(String msg, Throwable cause) {
		super(msg, cause);
//		super("[" + msg + "] : " + (cause==null?"":cause.getMessage()), cause);
	}

	public BaseException(String msg, Throwable cause, String code) {
		this(msg, cause);
		this.code = code;
	}

}
