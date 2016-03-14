package org.onetwo.boot.plugins.security.method;

import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

public class MethodWebExpressionVoter implements AccessDecisionVoter<MethodInvocation> {
	private SecurityExpressionHandler<MethodInvocation> expressionHandler = new DefaultMethodSecurityExpressionHandler();

	public int vote(Authentication authentication, MethodInvocation fi, Collection<ConfigAttribute> attributes) {
		assert authentication != null;
		assert fi != null;
		assert attributes != null;

		WebExpressionConfigAttribute weca = findConfigAttribute(attributes);

		if (weca == null) {
			return ACCESS_ABSTAIN;
		}

		EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication,
				fi);

		return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED
				: ACCESS_DENIED;
	}

	private WebExpressionConfigAttribute findConfigAttribute(
			Collection<ConfigAttribute> attributes) {
		for (ConfigAttribute attribute : attributes) {
			if (attribute instanceof WebExpressionConfigAttribute) {
				return (WebExpressionConfigAttribute) attribute;
			}
		}
		return null;
	}

	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof WebExpressionConfigAttribute;
	}

	public boolean supports(Class<?> clazz) {
		return MethodInvocation.class.isAssignableFrom(clazz);
	}

	public void setExpressionHandler(SecurityExpressionHandler<MethodInvocation> expressionHandler) {
		this.expressionHandler = expressionHandler;
	}

	public static class WebExpressionConfigAttribute implements ConfigAttribute {
		private final Expression authorizeExpression;

		public WebExpressionConfigAttribute(Expression authorizeExpression) {
			this.authorizeExpression = authorizeExpression;
		}

		Expression getAuthorizeExpression() {
			return authorizeExpression;
		}

		public String getAttribute() {
			return null;
		}

		@Override
		public String toString() {
			return authorizeExpression.getExpressionString();
		}
	}
}
