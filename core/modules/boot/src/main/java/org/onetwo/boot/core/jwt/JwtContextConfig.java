package org.onetwo.boot.core.jwt;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * enable的key少写了d，使用AllNestedConditions修复兼容 
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(value=JwtConfig.ENABLE_KEY, matchIfMissing=false, havingValue="true")
public class JwtContextConfig {
	
	@Autowired
	private BootJFishConfig jfishConfig;
	
	@Bean
	@ConditionalOnMissingBean(JwtMvcInterceptor.class)
	public JwtMvcInterceptor jwtMvcInterceptor(){
		JwtMvcInterceptor interceptor = new JwtMvcInterceptor();
		interceptor.setAuthHeaderName(jfishConfig.getJwt().getAuthHeader());
		return interceptor;
	}
	
	@Bean
	@ConditionalOnMissingBean(JwtTokenService.class)
	public JwtTokenService jwtTokenService(){
		SimpleJwtTokenService tokenService = new SimpleJwtTokenService();
		tokenService.setJwtConfig(jfishConfig.getJwt());
		return tokenService;
	}
	
	@Bean
	public JwtUserDetailArgumentResolver jwtUserDetailArgumentResolver(){
		return new JwtUserDetailArgumentResolver(jfishConfig.getJwt().getAuthHeader());
	}


	@Bean
	@ConditionalOnMissingBean(JwtSessionUserManager.class)
	public SessionUserManager<UserDetail> sessionUserManager(){
		return new JwtSessionUserManager(jfishConfig.getJwt().getAuthHeader());
	}
}
