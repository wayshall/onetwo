package org.onetwo.common.exception;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.utils.StringUtils;

/*********
 * 
 * @author wayshall
 *
 */
@SuppressWarnings("serial")
public class ApiClientException extends BaseException implements ExceptionCodeMark{
	public static final String BASE_CODE = "[ApiClient]";//前缀

	public static ApiClientException formatMessage(String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new ApiClientException(formatMsg);
	}
	public static ApiClientException formatMessage(Throwable cause, String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new ApiClientException(formatMsg, cause);
	}

	public static ApiClientException formatCodeMessage(String code, Object...args){
		return formatCodeMessage(null, code, args);
	}
	public static ApiClientException formatCodeMessage(Throwable cause, String code, Object...args){
		ApiClientException se = new ApiClientException(cause, code);
		se.args = args;
		return se;
	}
	
	protected String code;
	private Object[] args;
	private Integer statusCode;

	public ApiClientException() {
		super();
	} 

	public ApiClientException(String msg, String code) {
		super(msg);
		initErrorCode(code);
	}

	public ApiClientException(ErrorType exceptionType) {
		this(exceptionType, null);
	}

	public ApiClientException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType.getErrorMessage(), cause);
		initErrorCode(exceptionType.getErrorCode());
		this.statusCode = exceptionType.getStatusCode();
	}

	public ApiClientException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public ApiClientException(String msg) {
		super(msg);
	}


	public ApiClientException(Throwable cause, String code) {
		super(cause);
		initErrorCode(code);
	}

	
	final protected void initErrorCode(String code){
		if(StringUtils.isNotBlank(code))
			this.code = code;//appendBaseCode(code);
	}
	public String getCode() {
		if(StringUtils.isBlank(code))
			return BASE_CODE;
		return code;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}

}
