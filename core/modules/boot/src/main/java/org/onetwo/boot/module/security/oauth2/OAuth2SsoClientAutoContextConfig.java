package org.onetwo.boot.module.security.oauth2;

import org.onetwo.boot.module.security.oauth2.OAuth2SsoClientAutoContextConfig.EnableOauth2SsoCondition;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@ConditionalOnClass(EnableOAuth2Sso.class)
@Conditional(EnableOauth2SsoCondition.class)
public class OAuth2SsoClientAutoContextConfig {
	

	@Bean
	public PrincipalExtractor userDetailPrincipalExtractor(){
		return new UserDetailPrincipalExtractor();
	}
	

	public static class EnableOauth2SsoCondition extends NotEnableOauth2SsoCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionOutcome cond = super.getMatchOutcome(context, metadata);
			if(cond.isMatch()){
				return ConditionOutcome.noMatch("not @EnableOAuth2Sso bean found!");
			}else{
				return ConditionOutcome.match("@EnableOAuth2Sso sso client!");
			}
		}
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
