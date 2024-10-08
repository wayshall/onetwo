package org.onetwo.boot.module.security.config;

import org.onetwo.boot.core.BootWebCommonAutoConfig;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.ms.BootMSContextAutoConfig;
import org.onetwo.boot.core.web.BootWebUIContextAutoConfig;
import org.onetwo.boot.core.web.mvc.DefaultExceptionMessageFinder;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.interceptor.LoggerInterceptor;
import org.onetwo.boot.core.web.mvc.log.AccessLogProperties;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.boot.module.security.BootSecurityConfig;
import org.onetwo.boot.module.security.BootSecurityExceptionMessager;
import org.onetwo.boot.module.security.handler.BootSecurityAccessDeniedHandler;
import org.onetwo.boot.module.security.mvc.SecurityWebExceptionResolver;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.ext.security.SecurityExceptionMessager;
import org.onetwo.ext.security.ajax.AjaxSupportedAccessDeniedHandler;
import org.onetwo.ext.security.jwt.JwtContxtConfig;
import org.onetwo.ext.security.login.LoginSecurityConfigurer;
import org.onetwo.ext.security.login.WebFormLoginSecurityConfigurer;
import org.onetwo.ext.security.redis.RedisContextConfig;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.SecurityContextRepository;

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
	@Autowired(required=false)
	private ExceptionMessageAccessor exceptionMessageAccessor;
	@Autowired
	private SecurityConfig securityConfig;

	@Autowired
	protected BootJFishConfig jfishConfig;
	@Autowired(required=false)
	private BootJsonView jsonView;
	@Autowired
	private SessionUserManager<GenericUserDetail<?>> sessionUserManager;

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
	

//	@Bean
//	public JwtSecurityTokenService jwtSecurityTokenService(){
//		BootJwtSecurityTokenService jwtService = new BootJwtSecurityTokenService();
//		return jwtService;
//	}

	
	@Bean
	@ConditionalOnMissingBean(AjaxSupportedAccessDeniedHandler.class)
	public AjaxSupportedAccessDeniedHandler ajaxSupportedAccessDeniedHandler(){
		BootSecurityAccessDeniedHandler adh = new BootSecurityAccessDeniedHandler();
		adh.setErrorPage(securityConfig.getErrorPage());
		return adh;
	}

	@Bean
	@ConditionalOnMissingBean({LoggerInterceptor.class})
	@ConditionalOnProperty(value=AccessLogProperties.ENABLE_MVC_LOGGER_INTERCEPTOR, matchIfMissing=true, havingValue="true")
	public LoggerInterceptor loggerInterceptor(){
		LoggerInterceptor log = new LoggerInterceptor();
		log.setUserDetailRetriever(()->{
//			if(SecurityContextHolder.getContext().getAuthentication()==null)
//				return null;
//			Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			if(UserDetail.class.isInstance(user)){
//				return (UserDetail)user;
//			}else if(User.class.isInstance(user)){
//				User suser = (User)user;
//				SimpleUserDetail ud = new SimpleUserDetail();
//				ud.setUserName(suser.getUsername());
//				return ud;
//			}
//			return null;
			return sessionUserManager.getCurrentUser();
		});
		log.setPathPatterns(accessLogProperties.getPathPatterns());
		return log;
	}
	
	@Bean
	public SecurityExceptionMessager securityExceptionMessager(){
		return new BootSecurityExceptionMessager(new DefaultExceptionMessageFinder(exceptionMessageAccessor));
	}
	

	@Bean
	@ConditionalOnMissingBean(LoginSecurityConfigurer.class)
	public LoginSecurityConfigurer webFormLoginConfigurer() {
		return new WebFormLoginSecurityConfigurer();
	}

	@Bean(BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER)
//	@ConditionalOnMissingBean(name=BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER, value={BootWebExceptionResolver.class, ResponseEntityExceptionHandler.class})
	@ConditionalOnMissingBean(name=BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER, value={BootWebExceptionResolver.class})
	public SecurityWebExceptionResolver bootWebExceptionResolver(){
		SecurityWebExceptionResolver r = new SecurityWebExceptionResolver();
		r.setJfishConfig(jfishConfig);
		r.setErrorView(jsonView);
		return r;
	}

	@ConditionalOnProperty(name="hostName", prefix=org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".security.redis")
	@Configuration
	public static class BootRedisContextConfig extends RedisContextConfig {
	}

//	@ConditionalOnProperty(name="signingKey", prefix=org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".security.jwt")
	@ConditionalOnProperty(name={"signingKey", "enabled"}, prefix=org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".security.jwt")
	@Configuration
	@ConditionalOnMissingBean(SecurityContextRepository.class)
	public static class BootJwtContxtConfig extends JwtContxtConfig {
	}
	
}
