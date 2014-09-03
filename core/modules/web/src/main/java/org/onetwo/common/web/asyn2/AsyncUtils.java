package org.onetwo.common.web.asyn2;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;

public final class AsyncUtils {

	private final static String CALLBACK_PARAMETER = "asynCallback";
	

	public static AsynWebProcessor createAsynProcessor(HttpServletRequest request, HttpServletResponse response) {
		return createAsynProcessor(response, getAsyncCallbackName(request));
	}
	public static AsynWebProcessor createAsynProcessor(HttpServletResponse response, String asynCallback) {
		try {
			return new AsynWebProcessor(response.getWriter(), asynCallback);
		} catch (IOException e) {
			throw new BaseException("create AsynWebProcessor error: " + e.getMessage(), e);
		}
	}

	public static String getAsyncCallbackName(HttpServletRequest request){
		String asynCb = getRequestCallback(request);
		asynCb = getAsyncCallbackName(asynCb);
		return asynCb;
	}
	
	private static String getRequestCallback(HttpServletRequest request){
		String cbName = request.getParameter(CALLBACK_PARAMETER);
		if(StringUtils.isBlank(cbName))
			throw new BaseException("no asyn callback paramter!");
		return cbName;
	}

	public static String getAsyncCallbackName(String name){
		return "parent.$.jfish.asyncCallbackManager."+name;
	}
	
	private AsyncUtils(){}

}
