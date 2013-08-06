package org.onetwo.common.exception;

import java.io.Serializable;

import org.onetwo.common.utils.StringUtils;


@SuppressWarnings("serial")
public class BaseException extends RuntimeException implements SystemErrorCode, ExceptionCodeMark, Serializable{

	protected static final String DefaultMsg = "occur error";
	public static final String Prefix = "[ERROR]:";
	protected String code = DEFAULT_SYSTEM_ERROR_CODE;

//	protected List<Throwable> list = null;

	public BaseException() {
		super(DefaultMsg);
		this.initErrorCode(null);
	}
	
	final protected void initErrorCode(String code){
		if(code!=null)
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
		super("[" + msg + "] : " + cause.getMessage(), cause);
		this.initErrorCode(null);
	}

	public BaseException(String msg, Throwable cause, String code) {
		this(msg, cause);
		this.initErrorCode(code);
	}

	public String getCode() {
		return code;
	}
	
	protected String getBaseCode(){
//		return BaseErrorCode.BASE_CODE;
		return null;
	}

	final public String appendBaseCode(String code){
		String baseCode = getBaseCode();
//		Assert.hasText(baseCode, "base code can not be empty");
		if(baseCode==null)
			return code;
		if(StringUtils.isBlank(code))
			return baseCode;
		if(!code.startsWith(baseCode)){
			return baseCode + code;
		}else{
			return code;
		}
	}

}
