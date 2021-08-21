package org.onetwo.ext.security.method;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.ext.security.MultiExpressionVoter;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

/****
 * @author way
 */
public class MultiMethodExpressionVoter extends MultiExpressionVoter<MethodInvocation> implements AccessDecisionVoter<MethodInvocation> {
	private SecurityExpressionHandler<MethodInvocation> expressionHandler = new DefaultMethodSecurityExpressionHandler();

	public boolean supports(Class<?> clazz) {
		return MethodInvocation.class.isAssignableFrom(clazz);
	}

	public void setExpressionHandler(
			SecurityExpressionHandler<MethodInvocation> expressionHandler) {
		this.expressionHandler = expressionHandler;
	}

	@Override
	protected EvaluationContext createEvaluationContext(Authentication authentication, MethodInvocation invocation) {
		return expressionHandler.createEvaluationContext(authentication, invocation);
	}
	
}
