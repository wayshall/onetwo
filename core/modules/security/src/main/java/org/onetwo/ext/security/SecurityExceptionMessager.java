package org.onetwo.ext.security;


/**
 * @author wayshall
 * <br/>
 */
public interface SecurityExceptionMessager {

	String findMessageByErrorCode(String errorCode, Object...errorArgs);
	
	String findMessageByThrowable(Throwable e, Object...errorArgs);
}
