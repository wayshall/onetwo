package org.onetwo.boot.module.security.config;

import org.onetwo.boot.core.BootWebCommonAutoConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.ms.BootMSContextAutoConfig;
import org.onetwo.boot.core.web.BootWebUIContextAutoConfig;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.interceptor.LoggerInterceptor;
import org.onetwo.boot.core.web.mvc.log.AccessLogProperties;
import org.onetwo.boot.module.security.BootSecurityConfig;
import org.onetwo.boot.module.security.mvc.SecurityWebExceptionResolver;
import org.onetwo.common.web.userdetails.SimpleUserDetail;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.security.jwt.JwtContxtConfig;
import org.onetwo.ext.security.redis.RedisContextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/****
 * boot security的通用配置
 * jfish.security.redis 启动redis存储session
 * @author way
 *
 */
@EnableConfigurationProperties({BootSecurityConfig.class, AccessLogProperties.class})
@Configuration
@AutoConfigureAfter({BootMSContextAutoConfig.class, BootWebUIContextAutoConfig.class})
public class BootSecurityCommonContextConfig{
	@Autowired
	private AccessLogProperties accessLogProperties;

	/*@Autowired
	private BootSecurityConfig bootSecurityConfig;
	
	@Override
	public SecurityConfig getSecurityConfig() {
		return bootSecurityConfig;
	}*/
	
	/*@Bean
	public ExtRestTemplate extRestTemplate(){
		return new ExtRestTemplate();
	}*/

	@Bean
	@ConditionalOnMissingBean({LoggerInterceptor.class})
	@ConditionalOnBean(BootSiteConfig.class)
	@ConditionalOnProperty(value=AccessLogProperties.ENABLE_MVC_LOGGER_INTERCEPTOR, matchIfMissing=true, havingValue="true")
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
		log.setPathPatterns(accessLogProperties.getPathPatterns());
		return log;
	}

	@Bean(BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER)
	@ConditionalOnBean(BootSiteConfig.class)
	@ConditionalOnMissingBean(name=BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER, value={BootWebExceptionResolver.class, ResponseEntityExceptionHandler.class})
	public SecurityWebExceptionResolver bootWebExceptionResolver(){
		return new SecurityWebExceptionResolver();
	}

	@ConditionalOnProperty(name="hostName", prefix="jfish.security.redis")
	@Configuration
	public static class BootRedisContextConfig extends RedisContextConfig {
	}

	@ConditionalOnProperty(name="signingKey", prefix="jfish.security.jwt")
	@Configuration
	@ConditionalOnMissingBean(SecurityContextRepository.class)
	public static class BootJwtContxtConfig extends JwtContxtConfig {
	}
	
}
