package org.onetwo.common.web.preventor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

public interface PreventRequestInfoManager {

	public RequestPreventInfo getRequestPreventInfo(Method controller, HttpServletRequest request);

}