package org.onetwo.ext.security.utils;

import java.util.Set;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.security.matcher.MatcherUtils;
import org.onetwo.ext.security.matcher.MutipleRequestMatcher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

	public static LoginUserDetails getCurrentLoginUser(){
		return (LoginUserDetails)getCurrentLoginUser(SecurityContextHolder.getContext());
	}
	public static <T> T getCurrentLoginUser(Class<T> clazz){
		return clazz.cast(getCurrentLoginUser());
	}
	public static LoginUserDetails getCurrentLoginUser(SecurityContext context){
		Authentication auth = context.getAuthentication();
		return (LoginUserDetails)getCurrentLoginUser(auth);
	}
	public static LoginUserDetails getCurrentLoginUser(Authentication auth){
		if(auth==null || AnonymousAuthenticationToken.class.isInstance(auth))
			return null;
		return (LoginUserDetails)auth.getPrincipal();
	}
	
	public static Runnable runInThread(Runnable runnable){
		SecurityContext ctx = SecurityContextHolder.getContext();
		return ()->{
			SecurityContextHolder.setContext(ctx);
			try {
				runnable.run();
			} finally {
				SecurityContextHolder.clearContext();
			}
		};
	}
}
