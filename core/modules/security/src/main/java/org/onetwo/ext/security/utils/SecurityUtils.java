package org.onetwo.ext.security.utils;

import java.util.Set;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
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

import lombok.Getter;

final public class SecurityUtils {
//	private static final Logger logger = JFishLoggerFactory.getLogger(SecurityUtils.class);
	private static final Set<String> KEYWORDS = ImmutableSet.of("permitAll", "authenticated", "fullyAuthenticated", "denyAll", "is", "has");
	
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
		String auth = StringUtils.uncapitalize(authority);
		return KEYWORDS.stream().filter(key->auth.startsWith(key))
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
	
	public static enum SecurityErrors {
		AUTH_FAILED("认证失败"),
//		NOT_AUTHED("未认证的用户"),
		ACCESS_DENIED("未授权，访问拒绝"),
		NOT_TRUSTED_USER("不受信任的用户");//包括匿名和rememberMe的用户
		
		@Getter
		private final String label;

		private SecurityErrors(String label) {
			this.label = label;
		}
		
	}
}
