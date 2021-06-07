package org.onetwo.boot.module.oauth2.ssoclient;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

public class DisabledOauth2SsoCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String ssoClass = "org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso";
		boolean ssoClientAnnotationExists = ClassUtils.isPresent(ssoClass, null);
		if(!ssoClientAnnotationExists){
			return ConditionOutcome.match("EnableOAuth2Sso not exists!");
		}
		String[] beanNames = context.getBeanFactory().getBeanNamesForAnnotation(EnableOAuth2Sso.class);
		if(beanNames==null || beanNames.length==0){
			return ConditionOutcome.match("@EnableOAuth2Sso bean not found!");
		}
		return ConditionOutcome.noMatch("@EnableOAuth2Sso found!");
	}
	
}
