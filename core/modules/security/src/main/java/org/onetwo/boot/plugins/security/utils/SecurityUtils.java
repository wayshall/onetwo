package org.onetwo.boot.plugins.security.utils;

import java.util.Set;

import org.onetwo.boot.plugins.security.CommonReadMethodMatcher;
import org.onetwo.boot.plugins.security.matcher.MatcherUtils;
import org.onetwo.boot.plugins.security.matcher.MutipleRequestMatcher;
import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.common.collect.ImmutableSet;

final public class SecurityUtils {
//	private static final Logger logger = JFishLoggerFactory.getLogger(SecurityUtils.class);
	private static final Set<String> KEYWORDS = ImmutableSet.of("permitAll", "denyAll", "is", "has");
	
	public static final RequestMatcher READ_METHOD_MATCHER = new CommonReadMethodMatcher();
	
	public static RequestMatcher checkCsrfIfRequestNotMatch(String...paths){
		return MatcherUtils.notMatcher(antPathsAndReadMethodMatcher(paths));
	}
	
	public static RequestMatcher antPathsAndReadMethodMatcher(String...paths){
		MutipleRequestMatcher mutiple = MatcherUtils.matchAntPaths(paths);
		mutiple.addMatchers(READ_METHOD_MATCHER);
		return mutiple;
	}
	
	public static FormLoginConfigurer<HttpSecurity> hackFormLoginAuthFilter(FormLoginConfigurer<HttpSecurity> formLoginConfig, AbstractAuthenticationProcessingFilter filter){
		ReflectUtils.getIntro(FormLoginConfigurer.class).setFieldValue(formLoginConfig, "authFilter", filter);
		return formLoginConfig;
	}
	
	public static String createSecurityExpression(String authString){
		if(isKeyword(authString)){
			return authString;
		}
		StringBuilder str = new StringBuilder("hasAuthority('");
		str.append(authString).append("')");
		return str.toString();
	}
	
	protected static boolean isKeyword(String authority){
		return KEYWORDS.stream().filter(key->authority.startsWith(key))
						.findAny().isPresent();
	}
}
