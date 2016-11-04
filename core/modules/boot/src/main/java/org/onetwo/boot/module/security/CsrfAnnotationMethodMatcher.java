package org.onetwo.boot.module.security;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.web.preventor.DefaultPreventRequestInfoManager;
import org.onetwo.common.web.preventor.PreventRequestInfoManager;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.HandlerMethod;

public class CsrfAnnotationMethodMatcher implements RequestMatcher {
	private PreventRequestInfoManager csrfAnnotationManager = new DefaultPreventRequestInfoManager();
	private RequestMatcher excludeRequestMatcher;
    

    public CsrfAnnotationMethodMatcher() {
    }

    public boolean matches(HttpServletRequest request) {
    	if(isExclude(request)){
    		return false;
    	}
    	HandlerMethod handler = BootWebUtils.webHelper().getControllerHandler();
    	return csrfAnnotationManager.getRequestPreventInfo(handler.getMethod(), request).isCsrfValidate();
    }
    
    protected boolean isExclude(HttpServletRequest request){
    	if(excludeRequestMatcher==null)
    		return false;
    	return excludeRequestMatcher.matches(request);
    }

	final public void setExcludeUrls(String... excludeUrls) {
		this.excludeRequestMatcher = SecurityUtils.checkCsrfIfRequestNotMatch(excludeUrls);
	}
    
}
