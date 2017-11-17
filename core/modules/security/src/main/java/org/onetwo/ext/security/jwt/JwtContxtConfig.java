package org.onetwo.ext.security.jwt;

import org.onetwo.common.spring.condition.OnMissingBean;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.security.utils.CookieStorer;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * set jfish.security.jwt.signingKey to enable jwt
 * @see BootJwtContxtConfig 
 * 
 * @author wayshall
 * <br/>
 */
@Configuration
public class JwtContxtConfig {
	
	@Autowired
	private SecurityConfig securityConfig;
	
	@OnMissingBean(JwtSecurityTokenService.class)
	@Bean
	public JwtSecurityTokenService jwtTokenService(){
		JwtSecurityTokenService ts = new DefaultJwtSecurityTokenService();
		return ts;
	}
	
	@Bean
	@Autowired
	public SecurityContextRepository securityContextRepository(JwtSecurityTokenService jwtTokenService){
		JwtSecurityContextRepository jwt = new JwtSecurityContextRepository();
		jwt.setJwtTokenService(jwtTokenService);
		String authName = securityConfig.getJwt().getAuthKey();
		if(StringUtils.isBlank(authName)){
			authName = securityConfig.getJwt().getAuthHeader();
		}
		jwt.setAuthHeaderName(authName);
		jwt.setAuthStore(securityConfig.getJwt().getAuthStore());
		jwt.setCookieStorer(CookieStorer.builder()
										.cookieDomain(securityConfig.getCookie().getDomain())
										.cookiePath(securityConfig.getCookie().getPath())
										.build());
		return jwt;
	}
}
