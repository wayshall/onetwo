package org.onetwo.common.apiclient;

import java.io.IOException;
import java.util.Optional;

import org.onetwo.common.apiclient.interceptor.ApiInterceptorChain.ActionInvoker;
import org.onetwo.common.apiclient.utils.ApiClientConstants.ApiClientErrors;
import org.onetwo.common.exception.ApiClientException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;

import lombok.Data;

/**
 * api client 错误处理器
 * @author weishao zeng
 * <br/>
 */
public interface ApiErrorHandler {
	
	ApiErrorHandler DEFAULT = new DefaultErrorHandler();
	
	default Object handleError(ErrorInvokeContext ctx) {
		if (ctx.isIoError() && ctx.getRequestContext().isRetryable()) {
			try {
				return ctx.getRetryInvoker().invoke();
			} catch (Throwable e) {
				throw new BaseException("retry Invoker error: " + e.getMessage(), e);
			}
		} else {
			return handleError(ctx.getRequestContext().getInvokeMethod(), ctx.getError());
		}
	}
	default Object handleError(ApiClientMethod invokeMethod, Throwable e) {
		if (e instanceof ApiClientException) {
			throw (ApiClientException) e;
		} else {
			throw new ApiClientException(ApiClientErrors.EXECUTE_REST_ERROR, invokeMethod.getMethod(), e);
		}
	}
	

	public class DefaultErrorHandler implements ApiErrorHandler {
	}
	
	@Data
	public class ErrorInvokeContext {
		final private RequestContextData requestContext;
		final private Throwable error;
		final long retryWaitInMillis;
		
		/****
		 * 重试调用
		 * 在api错误处理中，有些特殊错误（如token过期之类）处理后（如重新获取）后，需要重新调用原来的请求
		 */
		private ActionInvoker retryInvoker;
		
		public boolean isIoError() {
			return findIOException().isPresent();
		}
		
		protected Optional<IOException> findIOException() {
			IOException e = LangUtils.getCauseException(error, IOException.class);
			return Optional.ofNullable(e);
		}
	}
}

