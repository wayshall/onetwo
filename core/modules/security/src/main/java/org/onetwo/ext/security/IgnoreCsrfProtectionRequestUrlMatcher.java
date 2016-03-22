package org.onetwo.ext.security;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class IgnoreCsrfProtectionRequestUrlMatcher implements RequestMatcher {
	
	public static IgnoreCsrfProtectionRequestUrlMatcher ignoreUrls(String...urls){
		IgnoreCsrfProtectionRequestUrlMatcher matcher = new IgnoreCsrfProtectionRequestUrlMatcher();
		matcher.setIgnoreUrls(urls);
		return matcher;
	}
	
	private RequestMatcher requestMatcher = SecurityUtils.READ_METHOD_MATCHER;
    

    public IgnoreCsrfProtectionRequestUrlMatcher() {
    }

    public boolean matches(HttpServletRequest request) {
    	return !isRequestMatche(request);
    }
    
    protected boolean isRequestMatche(HttpServletRequest request){
    	if(requestMatcher==null)
    		return false;
    	return requestMatcher.matches(request);
    }

	final public void setIgnoreUrls(String[] ignoreUrls) {
		this.requestMatcher = SecurityUtils.antPathsAndReadMethodMatcher(ignoreUrls);
	}

}
