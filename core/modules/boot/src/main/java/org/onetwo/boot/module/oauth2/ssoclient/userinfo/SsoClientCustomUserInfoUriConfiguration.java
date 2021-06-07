package org.onetwo.boot.module.oauth2.ssoclient.userinfo;

import org.onetwo.boot.module.oauth2.ssoclient.EnableOauth2SsoCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/***
 * 同时配置下面两个属性时生效：
 * oauth2: 
        resource: 
            preferTokenInfo: false
            userInfoUri: http://172.16.217.148:9000/SSO/oauth/me
 * @author way
 *
 */
@Configuration
@ConditionalOnClass(EnableOAuth2Sso.class)
@Conditional(EnableOauth2SsoCondition.class)
public class SsoClientCustomUserInfoUriConfiguration {
	

	@Bean
	@ConditionalOnMissingBean(PrincipalExtractor.class)
	public PrincipalExtractor userDetailPrincipalExtractor(){
		return new UserDetailPrincipalExtractor();
	}
	

	/*@Autowired
	private MultiWebExpressionVoter multiWebExpressionVoter;

	@Bean
	public AccessDecisionManager accessDecisionManager(){
		UnanimousBased unanimousBased = new UnanimousBased(Arrays.asList(multiWebExpressionVoter, 
																		new WebExpressionVoter(), 
																		new AuthenticatedVoter(),
																		new ScopeVoter()));
		return unanimousBased;
	}*/
}
