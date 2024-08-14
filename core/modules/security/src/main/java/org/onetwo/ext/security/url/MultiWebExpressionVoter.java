package org.onetwo.ext.security.url;

import org.onetwo.ext.security.MultiExpressionVoter;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/****
 * 可支持一个url映射到多个表达式
 * @author way
 *
 */
public class MultiWebExpressionVoter extends MultiExpressionVoter implements AccessDecisionVoter<Object> {

	private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();
	
	public MultiWebExpressionVoter(SecurityConfig securityConfig) {
		super(securityConfig);
	}
	
//	public boolean supports(Class<?> clazz) {
//		return FilterInvocation.class.isAssignableFrom(clazz);
//	}

	public void setExpressionHandler(
			SecurityExpressionHandler<FilterInvocation> expressionHandler) {
		this.expressionHandler = expressionHandler;
	}

	@Override
	protected EvaluationContext createEvaluationContext(Authentication authentication, Object invocation) {
		return expressionHandler.createEvaluationContext(authentication, (FilterInvocation)invocation);
	}
	
}
