package org.onetwo.common.apiclient;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import org.onetwo.common.apiclient.utils.ApiClientConstants.ApiClientErrors;
import org.onetwo.common.exception.ApiClientException;
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
		Optional<IOException> ioe = ctx.findIOException();
		if (ioe.isPresent() && ctx.getRequestContext().isRetryable()) {
			return ctx.getRetryInvoker().apply(ioe.get());
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
		
		private Function<IOException, Object> retryInvoker;
		
		public Optional<IOException> findIOException() {
			IOException e = LangUtils.getCauseException(error, IOException.class);
			return Optional.ofNullable(e);
		}
	}
}

