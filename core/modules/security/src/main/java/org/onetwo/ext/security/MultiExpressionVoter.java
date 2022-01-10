package org.onetwo.ext.security;

import java.util.Collection;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.userdetails.UserRoot;
import org.onetwo.ext.security.metadata.CodeSecurityConfig;
import org.slf4j.Logger;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

/****
 * 可支持一个url映射到多个表达式
 * @author way
 *
 */
abstract public class MultiExpressionVoter {
//	protected SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

	protected boolean isAnonymousUser(Authentication authentication) {
		return AnonymousAuthenticationToken.class.isInstance(authentication);
	}

	public boolean supports(Class<?> clazz) {
//		return MethodInvocation.class.isAssignableFrom(clazz);
		return true;
	}

	public int vote(Authentication authentication, Object invokcation,
			Collection<ConfigAttribute> attributes) {
		assert authentication != null;
		assert invokcation != null;
		assert attributes != null;
		
//		if (isAnonymousUser(authentication)) {
//			throw new AccessDeniedCodeException(JwtErrors.CM_NOT_LOGIN);
//		}

		if (authentication!=null && authentication.getDetails() instanceof UserRoot) {
			UserRoot user = (UserRoot) authentication.getDetails();
			if (user.isSystemRootUser()) {
				Logger logger = JFishLoggerFactory.getCommonLogger();
				if (logger.isInfoEnabled()) {
					logger.info("access granted for root user");
				}
				return AccessDecisionVoter.ACCESS_GRANTED;
			}
		}
//		CodeSecurityConfig codeConfig = findConfigAttribute(attributes);

		if (LangUtils.isEmpty(attributes)) {
			return AccessDecisionVoter.ACCESS_ABSTAIN;
		}

		// new WebSecurityExpressionRoot(authentication, fi),
		// 创建的EvaluationContext，其root对象是WebSecurityExpressionRoot(MethodSecurityExpressionRoot)，所以acces表达式，可以是WebSecurityExpressionRoot的任何方法，
		// 比如：permitAll()，isAuthenticated()，isFullyAuthenticated()，hasAuthority('authority')
		EvaluationContext ctx = createEvaluationContext(authentication, invokcation);

//		return ExpressionUtils.evaluateAsBoolean(codeConfig.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED
//				: ACCESS_DENIED;
		int result = AccessDecisionVoter.ACCESS_ABSTAIN;
		for (ConfigAttribute attribute : attributes) {
			if (attribute instanceof CodeSecurityConfig) {
				CodeSecurityConfig codeConfig = (CodeSecurityConfig) attribute;
				result = ExpressionUtils.evaluateAsBoolean(codeConfig.getAuthorizeExpression(), ctx) ? AccessDecisionVoter.ACCESS_GRANTED : AccessDecisionVoter.ACCESS_DENIED;
				if (result==AccessDecisionVoter.ACCESS_GRANTED) {
					return result;
				}
			}
		}
		return result;
	}

	abstract protected EvaluationContext createEvaluationContext(Authentication authentication, Object invocation);

	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof CodeSecurityConfig;
	}


}