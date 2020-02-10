package org.onetwo.cloud.hystrix.exception;

import java.io.PrintStream;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.ServiceException;

import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class HystrixBadRequestCodeException extends HystrixBadRequestException implements ExceptionCodeMark {
	
	public HystrixBadRequestCodeException(String message, ServiceException cause) {
		super(message, cause);
	}

	@Override
	public Object[] getArgs() {
		return getCuaseServiceException().getArgs();
	}

	@Override
	public String getCode() {
		return getCuaseServiceException().getCode();
	}
	
	public ServiceException getCuaseServiceException() {
		return (ServiceException) getCause();
	}

    public void printStackTrace(PrintStream s) {
    	Throwable cause = getCause();
    	if(cause instanceof BaseException){
    		BaseException be = (BaseException) cause;
        	be.errorContextToString().ifPresent(msg -> s.println(msg));
    	}
    	super.printStackTrace(s);
    }
}
