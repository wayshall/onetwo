package org.onetwo.boot.module.security.oauth2;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

public class NotEnableOauth2SsoCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String ssoClass = "org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso";
		boolean ssoClientAnnotationExists = ClassUtils.isPresent(ssoClass, null);
		if(!ssoClientAnnotationExists){
			return ConditionOutcome.match("EnableOAuth2Sso not exists!");
		}
		String[] beanNames = context.getBeanFactory().getBeanNamesForAnnotation(EnableOAuth2Sso.class);
		if(beanNames==null || beanNames.length==0){
			return ConditionOutcome.match("not @EnableOAuth2Sso bean found!");
		}
		return ConditionOutcome.noMatch("@EnableOAuth2Sso sso client!");
	}
	
}
