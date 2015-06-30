package org.onetwo.boot.security;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.web.preventor.DefaultPreventRequestInfoManager;
import org.onetwo.common.web.preventor.PreventRequestInfoManager;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.HandlerMethod;

public class CsrfAnnotationMethodMatcher implements RequestMatcher {
	private PreventRequestInfoManager csrfAnnotationManager = new DefaultPreventRequestInfoManager();
    

    public CsrfAnnotationMethodMatcher() {
    }


    public boolean matches(HttpServletRequest request) {
    	HandlerMethod handler = BootWebUtils.webHelper().getControllerHandler();
    	return csrfAnnotationManager.getRequestPreventInfo(handler.getMethod(), request).isCsrfValidate();
    }
}
