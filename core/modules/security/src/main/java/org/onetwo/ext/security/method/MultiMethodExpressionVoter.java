package org.onetwo.ext.security.method;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.ext.security.MultiExpressionVoter;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

/****
 * @author way
 */
public class MultiMethodExpressionVoter extends MultiExpressionVoter implements AccessDecisionVoter<Object> {
	private SecurityExpressionHandler<MethodInvocation> expressionHandler = new DefaultMethodSecurityExpressionHandler();


	public MultiMethodExpressionVoter(SecurityConfig securityConfig) {
		super(securityConfig);
	}
	
	
	public void setExpressionHandler(
			SecurityExpressionHandler<MethodInvocation> expressionHandler) {
		this.expressionHandler = expressionHandler;
	}

	@Override
	protected EvaluationContext createEvaluationContext(Authentication authentication, Object invocation) {
		return expressionHandler.createEvaluationContext(authentication, (MethodInvocation)invocation);
	}
	
}
