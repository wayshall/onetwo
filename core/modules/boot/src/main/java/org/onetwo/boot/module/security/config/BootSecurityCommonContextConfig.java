package org.onetwo.boot.module.security.config;

import org.onetwo.boot.core.web.mvc.interceptor.LoggerInterceptor;
import org.onetwo.boot.module.security.BootSecurityConfig;
import org.onetwo.boot.module.security.mvc.SecurityWebExceptionResolver;
import org.onetwo.common.web.userdetails.SimpleUserDetail;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.security.redis.RedisContextConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/****
 * boot security的通用配置
 * jfish.security.redis 启动redis存储session
 * @author way
 *
 */
@EnableConfigurationProperties({BootSecurityConfig.class})
@Configuration
public class BootSecurityCommonContextConfig{

	/*@Autowired
	private BootSecurityConfig bootSecurityConfig;
	
	@Override
	public SecurityConfig getSecurityConfig() {
		return bootSecurityConfig;
	}*/

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
	
	@ConditionalOnProperty(name="hostName", prefix="jfish.security.redis")
	@Configuration
	public static class BootRedisContextConfig extends RedisContextConfig {
	}
	
}
