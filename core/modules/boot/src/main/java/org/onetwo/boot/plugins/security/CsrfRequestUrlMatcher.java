package org.onetwo.boot.plugins.security;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.plugins.security.utils.SecurityUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CsrfRequestUrlMatcher implements RequestMatcher {
	private RequestMatcher excludeRequestMatcher = SecurityUtils.READ_METHOD_MATCHER;
    

    public CsrfRequestUrlMatcher() {
    }

    public boolean matches(HttpServletRequest request) {
    	return !isExclude(request);
    }
    
    protected boolean isExclude(HttpServletRequest request){
    	if(excludeRequestMatcher==null)
    		return false;
    	return excludeRequestMatcher.matches(request);
    }

	final public void setExcludeUrls(String[] excludeUrls) {
		this.excludeRequestMatcher = SecurityUtils.antPathsAndReadMethodMatcher(excludeUrls);
	}

}
