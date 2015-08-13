package org.onetwo.boot.plugins.security.config;

import org.onetwo.boot.core.web.mvc.interceptor.LoggerInterceptor;
import org.onetwo.boot.plugins.security.BootSecurityConfig;
import org.onetwo.boot.plugins.security.mvc.SecurityWebExceptionResolver;
import org.onetwo.boot.plugins.security.mvc.args.SecurityArgumentResolver;
import org.onetwo.boot.plugins.security.utils.SecuritySessionUserManager;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.SimpleUserDetail;
import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/****
 * security的通用配置
 * @author way
 *
 */
@EnableConfigurationProperties({BootSecurityConfig.class})
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
	@ConditionalOnMissingBean(SecurityWebExceptionResolver.class)
	public SecurityWebExceptionResolver bootWebExceptionResolver(){
		return new SecurityWebExceptionResolver();
	}
	
	@Bean
	@ConditionalOnMissingBean(SecuritySessionUserManager.class)
	public SessionUserManager<UserDetail> sessionUserManager(){
		return new SecuritySessionUserManager();
	}
	
	@Bean
	@ConditionalOnMissingBean(BCryptPasswordEncoder.class)
	public BCryptPasswordEncoder bcryptEncoder(){
		BCryptPasswordEncoder coder = new BCryptPasswordEncoder();
		return coder;
	}

}
