package org.onetwo.boot.module.oauth2ssoclient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(OAuth2SsoClientProperties.class)
public class OAuth2SsoClientConfiguration {

	@Bean
	@ConditionalOnMissingBean(OAuth2SsoClientUserRepository.class)
	public OAuth2SsoClientUserRepository<OAuth2SsoClientUserInfo> oauth2SsoClientUserRepository(){
		return new HttpRequestOAuth2UserRepository<OAuth2SsoClientUserInfo>();
	}
}
