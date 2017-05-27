package org.onetwo.ext.security.utils;

import javax.servlet.http.HttpServletRequest;

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
    	return !isIgnoreMatche(request);
    }
    
    /****
     * 
     * @param request
     * @return true 表示匹配请求，会被忽略csrf验证，false表示需要验证
     */
    protected boolean isIgnoreMatche(HttpServletRequest request){
    	//如果为null，返回false，表示全部请求都要匹配验证
    	if(requestMatcher==null)
    		return false;
    	return requestMatcher.matches(request);
    }

	final public void setIgnoreUrls(String[] ignoreUrls) {
		this.requestMatcher = SecurityUtils.antPathsAndReadMethodMatcher(ignoreUrls);
	}

}
