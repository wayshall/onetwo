package org.onetwo.common.web.asyn2;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.web.asyn.AsyncUtils;

public final class AsyncWppFactory {
	

	public static AsyncWebProgressProcessor createAsynProcessor(HttpServletRequest request, HttpServletResponse response) {
		return createAsynProcessor(response, AsyncUtils.getAsyncCallbackName(request));
	}

	public static AsyncWebProgressProcessor createAsynProcessor(HttpServletResponse response, String asynCallback) {
		return createAsynProcessor(AsyncUtils.CONTENT_TYPE, response, asynCallback);
	}
	public static AsyncWebProgressProcessor createAsynProcessor(String contentType, HttpServletResponse response, String asynCallback) {
		try {
			response.setContentType(contentType);
			return new AsyncWebProgressProcessor(response.getWriter(), asynCallback);
		} catch (IOException e) {
			throw new BaseException("create AsynWebProcessor error: " + e.getMessage(), e);
		}
	}
	private AsyncWppFactory(){}

}
