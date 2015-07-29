package org.onetwo.boot.plugins.security.config;

import org.onetwo.boot.core.web.mvc.interceptor.LoggerInterceptor;
import org.onetwo.boot.plugins.security.mvc.SecurityWebExceptionResolver;
import org.onetwo.boot.plugins.security.mvc.args.SecurityArgumentResolver;
import org.onetwo.common.utils.SimpleUserDetail;
import org.onetwo.common.utils.UserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@Configuration
public class SecurityCommonContextConfig {
	
	@Bean
	public HandlerMethodArgumentResolver securityArgumentResolver(){
		return new SecurityArgumentResolver();
	}
	
	@Bean
	public LoggerInterceptor loggerInterceptor(){
		LoggerInterceptor log = new LoggerInterceptor();
		log.setUserDetailRetriever(()->{
			if(SecurityContextHolder.getContext().getAuthentication()==null)
				return null;
			Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(UserDetail.class.isInstance(user)){
				return (UserDetail)user;
			}else if(User.class.isInstance(user)){
				User suser = (User)user;
				SimpleUserDetail ud = new SimpleUserDetail();
				ud.setUserName(suser.getUsername());
				return ud;
			}
			return null;
		});
		return log;
	}

	@Bean
	public SecurityWebExceptionResolver bootWebExceptionResolver(){
		return new SecurityWebExceptionResolver();
	}

}
