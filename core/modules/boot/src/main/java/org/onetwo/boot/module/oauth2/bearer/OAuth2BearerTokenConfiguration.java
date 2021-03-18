package org.onetwo.boot.module.oauth2.bearer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(BearerTokenProperties.class)
public class OAuth2BearerTokenConfiguration {
	
	@Bean
	public BearerTokenExtractor oauth2BearerTokenExtractor() {
		return new OAuth2BearerTokenExtractor();
	}

}

