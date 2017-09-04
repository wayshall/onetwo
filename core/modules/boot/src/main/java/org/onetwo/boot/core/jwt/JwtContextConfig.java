package org.onetwo.boot.core.jwt;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
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
		return new JwtMvcInterceptor();
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
		return new JwtUserDetailArgumentResolver();
	}

}
