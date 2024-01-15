package org.onetwo.ext.security.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.security.matcher.MatcherUtils;
import org.onetwo.ext.security.matcher.MutipleRequestMatcher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.common.collect.ImmutableSet;

final public class SecurityUtils {
//	private static final Logger logger = JFishLoggerFactory.getLogger(SecurityUtils.class);
	private static final Set<String> KEYWORDS = ImmutableSet.of("permitAll", "authenticated", "fullyAuthenticated", "denyAll", "is", "has");
	
	public static final RequestMatcher READ_METHOD_MATCHER = new CommonReadMethodMatcher();
	
	public static Collection<? extends GrantedAuthority> createAuthorityList(String roles) {
		return createAuthorityList(roles, ",");
	}
    public static Collection<? extends GrantedAuthority> createAuthorityList(String roles, String splitor) {
    	if (StringUtils.isBlank(roles)) {
    		return Collections.emptyList();
    	}
    	String[] authorities = GuavaUtils.split(roles, splitor);
    	List<GrantedAuthority> authList = AuthorityUtils.createAuthorityList(authorities);
    	return authList;
    }
	
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
		if (StringUtils.isBlank(authority)) {
			throw new IllegalArgumentException("authority can not be blank!");
		}
		String auth = StringUtils.uncapitalize(authority);
		return KEYWORDS.stream().filter(key->auth.startsWith(key))
						.findAny().isPresent();
	}

	public static GenericLoginUserDetails<?> getCurrentLoginUser(){
		return (GenericLoginUserDetails<?>)getCurrentLoginUser(SecurityContextHolder.getContext());
	}
	public static <T> T getCurrentLoginUser(Class<T> clazz){
		return clazz.cast(getCurrentLoginUser());
	}
	public static GenericLoginUserDetails<?> getCurrentLoginUser(SecurityContext context){
		Authentication auth = context.getAuthentication();
		return (GenericLoginUserDetails<?>)getCurrentLoginUser(auth);
	}
	public static GenericLoginUserDetails<?> getCurrentLoginUser(Authentication auth){
		if(auth==null || AnonymousAuthenticationToken.class.isInstance(auth))
			return null;
		return (GenericLoginUserDetails<?>)auth.getPrincipal();
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
