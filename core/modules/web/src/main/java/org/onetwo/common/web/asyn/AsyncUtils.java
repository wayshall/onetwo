package org.onetwo.common.web.asyn;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;

public final class AsyncUtils {
	public static final String CONTENT_TYPE = "text/html;charset=utf-8";
//	public static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

	private final static String CALLBACK_PARAMETER = "asynCallback";
	

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
		return "parent.asyncCallbackManager."+name;
	}
	
	private AsyncUtils(){}

}
