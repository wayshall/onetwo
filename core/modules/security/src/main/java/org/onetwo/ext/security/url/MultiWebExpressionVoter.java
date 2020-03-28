package org.onetwo.ext.security.url;

import java.util.Collection;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.userdetails.UserRoot;
import org.onetwo.ext.security.metadata.DatabaseSecurityMetadataSource.CodeSecurityConfig;
import org.slf4j.Logger;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/****
 * 可支持一个url映射到多个表达式
 * @author way
 *
 */
public class MultiWebExpressionVoter implements AccessDecisionVoter<FilterInvocation> {
	private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

	public int vote(Authentication authentication, FilterInvocation fi,
			Collection<ConfigAttribute> attributes) {
		assert authentication != null;
		assert fi != null;
		assert attributes != null;

		if (authentication!=null && authentication.getDetails() instanceof UserRoot) {
			UserRoot user = (UserRoot) authentication.getDetails();
			if (user.isSystemRootUser()) {
				Logger logger = JFishLoggerFactory.getCommonLogger();
				if (logger.isInfoEnabled()) {
					logger.info("access granted for root user");
				}
				return ACCESS_GRANTED;
			}
		}
//		CodeSecurityConfig codeConfig = findConfigAttribute(attributes);

		if (LangUtils.isEmpty(attributes)) {
			return ACCESS_ABSTAIN;
		}

		EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication,
				fi);

//		return ExpressionUtils.evaluateAsBoolean(codeConfig.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED
//				: ACCESS_DENIED;
		int result = ACCESS_ABSTAIN;
		for (ConfigAttribute attribute : attributes) {
			if (attribute instanceof CodeSecurityConfig) {
				CodeSecurityConfig codeConfig = (CodeSecurityConfig) attribute;
				result = ExpressionUtils.evaluateAsBoolean(codeConfig.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED : ACCESS_DENIED;
				if (result==ACCESS_GRANTED) {
					return result;
				}
			}
		}
		return result;
	}

	private CodeSecurityConfig findConfigAttribute(
			Collection<ConfigAttribute> attributes) {
		for (ConfigAttribute attribute : attributes) {
			if (attribute instanceof CodeSecurityConfig) {
				return (CodeSecurityConfig) attribute;
			}
		}
		return null;
	}

	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof CodeSecurityConfig;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	public void setExpressionHandler(
			SecurityExpressionHandler<FilterInvocation> expressionHandler) {
		this.expressionHandler = expressionHandler;
	}
	
}
