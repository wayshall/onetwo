package org.onetwo.common.exception;
/**
 * @author wayshall
 * <br/>
 */
public abstract class ErrorTypes {
	
	public static ErrorType of(String errorCode, String errorMessage){
		return new ErrorTypeImpl(errorCode, errorMessage, null);
	}
	
	public static ErrorType of(String errorCode, String errorMessage, Integer statusCode){
		return new ErrorTypeImpl(errorCode, errorMessage, statusCode);
	}

}
