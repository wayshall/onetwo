package org.onetwo.ext.security.utils;

import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class CommonReadMethodMatcher implements RequestMatcher {
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    

    public CommonReadMethodMatcher() {
    }


    public boolean matches(HttpServletRequest request) {
    	return allowedMethods.matcher(request.getMethod()).matches();
    }
}
