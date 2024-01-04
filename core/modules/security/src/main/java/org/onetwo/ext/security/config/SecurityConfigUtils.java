package org.onetwo.ext.security.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityConfig.InterceptersConfig;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

public class SecurityConfigUtils {
	
	public static void checkAndConfigIntercepterUrls(HttpSecurity http, SecurityConfig securityConfig) throws Exception {
		// permitAll
		if (securityConfig.isCheckAnyUrlpermitAll()) {
			for(Entry<String[], String> entry : securityConfig.getIntercepterUrls().entrySet()) {
				if (ArrayUtils.contains(entry.getKey(), "/**") && 
						("permitAll".equals(entry.getValue()) || "authenticated".equals(entry.getValue()))
					) {
					throw new BaseException("do not config /** -> permitAll or authenticated, it's very danger!");
				}
			}
		}
		configIntercepterUrls(http, securityConfig.getIntercepterUrls(), securityConfig.getIntercepters());
	}

	public static void configIntercepterUrls(HttpSecurity http, Map<String[], String> intercepterUrls, List<InterceptersConfig> intercepters) throws Exception {
		if(LangUtils.isNotEmpty(intercepterUrls)){
			for(Entry<String[], String> entry : intercepterUrls.entrySet()){
//				http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
				http.authorizeHttpRequests(authz -> {
					authz.requestMatchers(entry.getKey()).access(accessExpression(entry.getValue()));
				});
			}
		}
		
		if(LangUtils.isNotEmpty(intercepters)){
			for(InterceptersConfig interConfig : intercepters){
//				http.authorizeRequests().antMatchers(interConfig.getPathPatterns()).access(interConfig.getAccess());
				http.authorizeHttpRequests(authz -> {
					authz.requestMatchers(interConfig.getPathPatterns()).access(accessExpression(interConfig.getAccess()));
				});
			}
		}
	}

	public static void defaultAnyRequest(HttpSecurity http, String anyRequest) throws Exception {
		//其它未标记管理的功能的默认权限
		if(StringUtils.isBlank(anyRequest)){
			http.authorizeHttpRequests(authz -> {
				authz.anyRequest().authenticated();//需要登录
			});
//				.anyRequest()
//				.authenticated()//需要登录
				//.fullyAuthenticated()//需要登录，并且不是rememberme的方式登录
		}else if(SecurityConfig.ANY_REQUEST_NONE.equals(anyRequest)){
			//not config anyRequest
		}else{
//			http.authorizeRequests()
//				.anyRequest()
//				.access(anyRequest);
			http.authorizeHttpRequests(authz -> {
				authz.anyRequest().access(accessExpression(anyRequest));
			});
		}
	}
	
	public static WebExpressionAuthorizationManager accessExpression(String accessExpression) {
		return new WebExpressionAuthorizationManager(accessExpression);
	}

}
