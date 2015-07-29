package org.onetwo.boot.plugins.security.utils;

import org.onetwo.boot.core.matcher.MatcherUtils;
import org.onetwo.boot.core.matcher.MutipleRequestMatcher;
import org.onetwo.boot.plugins.security.CommonReadMethodMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

final public class SecurityUtils {
//	private static final Logger logger = JFishLoggerFactory.getLogger(SecurityUtils.class);
	
	public static final RequestMatcher READ_METHOD_MATCHER = new CommonReadMethodMatcher();
	
	public static RequestMatcher checkCsrfIfRequestNotMatch(String...paths){
		return MatcherUtils.notMatcher(antPathsAndReadMethodMatcher(paths));
	}
	
	public static RequestMatcher antPathsAndReadMethodMatcher(String...paths){
		MutipleRequestMatcher mutiple = MatcherUtils.matchAntPaths(paths);
		mutiple.addMatchers(READ_METHOD_MATCHER);
		return mutiple;
	}
}
