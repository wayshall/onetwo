package org.onetwo.cloud.feign;

import org.onetwo.common.exception.ServiceException;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
public class RetryServiceException extends ServiceException {
	//header
	public static final String RETRY_AFTER = "Retry-After";

	public RetryServiceException(String message, int deltaMillis) {
		super(message);
		header(RETRY_AFTER, String.valueOf(deltaMillis));
	}

}
