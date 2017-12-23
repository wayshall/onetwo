package org.onetwo.boot.module.oauth2.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class TokenServiceConfiguration {
	@Autowired
	private TokenStore tokenStore;

	@Bean
	@ConditionalOnMissingBean(ResourceServerTokenServices.class)
	public DefaultTokenServices oauth2ResourceServerTokenServices() {
		DefaultTokenServices services = new DefaultTokenServices();
		services.setTokenStore(tokenStore);
		return services;
	}

}
