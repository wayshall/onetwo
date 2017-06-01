package org.onetwo.common.exception;
/**
 * 枚举异常类可继承
 * @author wayshall
 * <br/>
 */
public interface ErrorType {
	
	String getErrorCode();
	String getErrorMessage();
	default Integer getStatusCode(){
		return null;
	};

}
