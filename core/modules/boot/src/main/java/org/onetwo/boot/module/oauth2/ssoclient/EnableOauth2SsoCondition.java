package org.onetwo.boot.module.oauth2.ssoclient;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author weishao zeng
 * <br/>
 */
public class EnableOauth2SsoCondition extends DisabledOauth2SsoCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		ConditionOutcome cond = super.getMatchOutcome(context, metadata);
		if(cond.isMatch()){
			return ConditionOutcome.noMatch("@EnableOAuth2Sso bean not found!");
		}else{
			return ConditionOutcome.match("@EnableOAuth2Sso bean found!");
		}
	}
}
