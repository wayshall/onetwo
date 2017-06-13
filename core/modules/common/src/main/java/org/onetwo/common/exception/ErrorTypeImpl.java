package org.onetwo.common.exception;
/**
 * @author wayshall
 * <br/>
 */
public class ErrorTypeImpl implements ErrorType {
	private final String errorCode;
	private final String errorMessage;
	private final Integer statusCode;
	public ErrorTypeImpl(String errorCode, String errorMessage,
			Integer statusCode) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public Integer getStatusCode() {
		return statusCode;
	}

}
