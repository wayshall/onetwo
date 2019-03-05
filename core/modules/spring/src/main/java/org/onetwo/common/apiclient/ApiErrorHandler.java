package org.onetwo.common.apiclient;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.apiclient.utils.ApiClientConstants.ApiClientErrors;
import org.onetwo.common.exception.ApiClientException;

/**
 * api client 错误处理器
 * @author weishao zeng
 * <br/>
 */
public interface ApiErrorHandler {
	
	ApiErrorHandler DEFAULT = new DefaultErrorHandler();
	
	default Object handleError(MethodInvocation invocation, ApiClientMethod invokeMethod, Exception e) {
		return handleError(invokeMethod, e);
	}
	default Object handleError(ApiClientMethod invokeMethod, Exception e) {
		if (e instanceof ApiClientException) {
			throw (ApiClientException) e;
		} else {
			throw new ApiClientException(ApiClientErrors.EXECUTE_REST_ERROR, invokeMethod.getMethod(), e);
		}
	}

	public class DefaultErrorHandler implements ApiErrorHandler {
	}
}

