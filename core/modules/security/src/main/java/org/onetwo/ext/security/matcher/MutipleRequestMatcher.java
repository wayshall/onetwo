package org.onetwo.ext.security.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.utils.LangUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class MutipleRequestMatcher implements RequestMatcher {
	
	public static class NotRequestMatcher implements RequestMatcher {
		final private RequestMatcher matcher;

		public NotRequestMatcher(RequestMatcher matcher) {
	        super();
	        this.matcher = matcher;
        }

		@Override
        public boolean matches(HttpServletRequest request) {
	        return !matcher.matches(request);
        }
		
	}
	
//	private PreventRequestInfoManager preventRequestInfoManager = new DefaultPreventRequestInfoManager(); 
	
    private List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();
	
	public MutipleRequestMatcher(List<RequestMatcher> matchers) {
	    super();
	    if(LangUtils.isNotEmpty(matchers))
	    	this.matchers.addAll(matchers);
    }

	public MutipleRequestMatcher(RequestMatcher... matchers) {
		super();
	    if(!LangUtils.isEmpty(matchers))
	    	this.matchers.addAll(Arrays.asList(matchers));
	}

	@Override
    public boolean matches(HttpServletRequest request) {
		for(RequestMatcher matcher : matchers){
			if(matcher.matches(request)){
				return true;
			}
		}
		return false;
    }
	
	public MutipleRequestMatcher addMatchers(RequestMatcher...matchers){
		this.matchers.addAll(Arrays.asList(matchers));
		return this;
	}

}
