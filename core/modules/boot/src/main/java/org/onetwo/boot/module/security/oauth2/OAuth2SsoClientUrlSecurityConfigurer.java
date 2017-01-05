package org.onetwo.boot.module.security.oauth2;

import org.onetwo.ext.security.DefaultUrlSecurityConfigurer;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;

@Configuration
@EnableOAuth2Sso
public class OAuth2SsoClientUrlSecurityConfigurer extends DefaultUrlSecurityConfigurer {

	public OAuth2SsoClientUrlSecurityConfigurer(AccessDecisionManager accessDecisionManager) {
		super(accessDecisionManager);
	}

}
