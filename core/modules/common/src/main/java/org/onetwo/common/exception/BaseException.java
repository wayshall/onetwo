package org.onetwo.common.exception;

import java.io.Serializable;

import org.onetwo.common.utils.StringUtils;


@SuppressWarnings("serial")
public class BaseException extends RuntimeException implements SystemErrorCode, ExceptionCodeMark, Serializable{

	protected static final String DefaultMsg = "occur error";
	public static final String Prefix = "[ERROR]:";
	protected String code;

//	protected List<Throwable> list = null;

	public BaseException() {
		super(DefaultMsg);
		this.initErrorCode(null);
	}
	
	final protected void initErrorCode(String code){
		if(StringUtils.isNotBlank(code))
			this.code = code;//appendBaseCode(code);
	}

	public BaseException(String msg) {
		super(Prefix + msg);
		this.initErrorCode(null);
	}

	public BaseException(String msg, String code) {
		this(msg);
		this.initErrorCode(code);
	}

	public BaseException(Throwable cause) {
		super(DefaultMsg, cause);
		this.initErrorCode(null);
	}

	public BaseException(Throwable cause, String code) {
		this(cause);
		this.initErrorCode(code);
	}

	public BaseException(String msg, Throwable cause) {
		super("[" + msg + "] : " + (cause==null?"":cause.getMessage()), cause);
		this.initErrorCode(null);
	}

	public BaseException(String msg, Throwable cause, String code) {
		this(msg, cause);
		this.initErrorCode(code);
	}

	public String getCode() {
		if(StringUtils.isBlank(code))
			return getDefaultCode();
		return code;
	}
	
	protected String getDefaultCode(){
		return DEFAULT_SYSTEM_ERROR_CODE;
	}

}
