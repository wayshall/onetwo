package org.onetwo.ext.security.jwt;

import org.onetwo.common.spring.condition.OnMissingBean;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class JwtContxtConfig {
	
	@Autowired
	private SecurityConfig securityConfig;
	
	@OnMissingBean(JwtTokenService.class)
	@Bean
	public JwtTokenService jwtTokenService(){
		JwtTokenService ts = new DefaultJwtTokenService();
		return ts;
	}
	
	@Bean
	@Autowired
	public SecurityContextRepository securityContextRepository(JwtTokenService jwtTokenService){
		JwtSecurityContextRepository jwt = new JwtSecurityContextRepository();
		jwt.setJwtTokenService(jwtTokenService);
		jwt.setAuthHeaderName(securityConfig.getJwt().getAuthHeader());
		return jwt;
	}
}
