package org.onetwo.ext.security.url;

import java.util.Collection;
import java.util.function.Supplier;

import org.onetwo.ext.security.config.SecurityConfigUtils;
import org.onetwo.ext.security.metadata.CodeSecurityConfig;
import org.onetwo.ext.security.metadata.JdbcSecurityMetadataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.ExpressionAuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * @author wayshall
 * <br/>
 */
public class DatabaseAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	@Autowired
	private JdbcSecurityMetadataSourceBuilder jdbcSecurityMetadataSource;
	private DefaultHttpSecurityExpressionHandler securityExpressionHandler = new DefaultHttpSecurityExpressionHandler();

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
		Collection<ConfigAttribute> permList = jdbcSecurityMetadataSource.getAttributes(context);
		if (permList.isEmpty()) {
			return SecurityConfigUtils.ALLOW;
		}
		
		EvaluationContext ctx = securityExpressionHandler.createEvaluationContext(authentication.get(), context);
		for (ConfigAttribute perm : permList) {
			if (!CodeSecurityConfig.class.isInstance(perm)) {
				continue;
			}
			
			CodeSecurityConfig codePerm = (CodeSecurityConfig) perm;
			boolean granted = ExpressionUtils.evaluateAsBoolean(codePerm.getAuthorizeExpression(), ctx);
			if (granted) {
				return new ExpressionAuthorizationDecision(granted, codePerm.getAuthorizeExpression());
			}
		}
		return SecurityConfigUtils.DENY;
	}

}
