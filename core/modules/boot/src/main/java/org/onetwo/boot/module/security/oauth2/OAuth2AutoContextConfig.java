package org.onetwo.boot.module.security.oauth2;

import java.util.Arrays;

import org.onetwo.ext.security.url.MultiWebExpressionVoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.oauth2.provider.vote.ScopeVoter;
import org.springframework.security.web.access.expression.WebExpressionVoter;

@Configuration
public class OAuth2AutoContextConfig {
	
	@Autowired
	private MultiWebExpressionVoter multiWebExpressionVoter;

	@Bean
	public AccessDecisionManager accessDecisionManager(){
		UnanimousBased unanimousBased = new UnanimousBased(Arrays.asList(multiWebExpressionVoter, 
																		new WebExpressionVoter(), 
																		new AuthenticatedVoter(),
																		new ScopeVoter()));
		return unanimousBased;
	}
}
