package org.onetwo.common.web.asyn;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;

public final class AsyncWppFactory {
	

	public static DefaultProgressAsyncWebProcessor createAsynProcessor(HttpServletRequest request, HttpServletResponse response) {
		return createAsynProcessor(response, AsyncUtils.getAsyncCallbackName(request));
	}

	public static DefaultProgressAsyncWebProcessor createAsynProcessor(HttpServletResponse response, String asynCallback) {
		return createAsynProcessor(AsyncUtils.CONTENT_TYPE, response, asynCallback);
	}
	public static DefaultProgressAsyncWebProcessor createAsynProcessor(String contentType, HttpServletResponse response, String asynCallback) {
		try {
			response.setContentType(contentType);
			return new DefaultProgressAsyncWebProcessor(response.getWriter(), asynCallback);
		} catch (IOException e) {
			throw new BaseException("create AsynWebProcessor error: " + e.getMessage(), e);
		}
	}
	private AsyncWppFactory(){}

}
