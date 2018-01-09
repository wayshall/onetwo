package org.onetwo.common.exception;

import java.lang.reflect.Method;
import java.util.Optional;

import org.onetwo.common.utils.StringUtils;

/*********
 * 
 * @author wayshall
 *
 */
@SuppressWarnings("serial")
public class ApiClientException extends BaseException implements ExceptionCodeMark{
	public static final String BASE_CODE = "ApiClient_";//前缀

	protected String code;
	private Object[] args;
	private Integer statusCode;

	public ApiClientException(ErrorType exceptionType) {
		this(exceptionType, (Throwable)null);
	}

	public ApiClientException(ErrorType exceptionType, Method method, Throwable cause) {
		super(String.format(exceptionType.getErrorMessage(), method), cause);
		initErrorCode(exceptionType.getErrorCode());
		this.statusCode = exceptionType.getStatusCode();
		put("api method", method);
	}

	public ApiClientException(ErrorType exceptionType, Class<?> interfaceClass, Throwable cause) {
		super(String.format(exceptionType.getErrorMessage(), interfaceClass.getSimpleName()), cause);
		initErrorCode(exceptionType.getErrorCode());
		this.statusCode = exceptionType.getStatusCode();
		put("api interface", interfaceClass);
	}

	public ApiClientException(ErrorType exceptionType, Throwable cause) {
		super(exceptionType.getErrorMessage(), cause);
		initErrorCode(exceptionType.getErrorCode());
		this.statusCode = exceptionType.getStatusCode();
	}

	public ApiClientException(String message, String code) {
		super(message);
		this.code = code;
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
	
	public Optional<Integer> getStatusCode() {
		return Optional.ofNullable(statusCode);
	}
	@Override
	public String toString() {
		return "ApiClientException [" + code + ", " + getMessage() +  "]";
	}

}
