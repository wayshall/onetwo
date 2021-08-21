package org.onetwo.ext.security.metadata;

import org.onetwo.ext.security.metadata.DatabaseSecurityMetadataSource.AuthorityResource;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.expression.Expression;
import org.springframework.security.access.SecurityConfig;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class CodeSecurityConfig extends SecurityConfig {
//	private final AuthorityResource auth;
	private final String authority;
	private final String authorityName;
	private final Expression authorizeExpression;
	// for sprig security 4.1, 可支持变量url 
//	private final EvaluationContextPostProcessor<FilterInvocation> postProcessor;
	
	public CodeSecurityConfig(AuthorityResource auth, Expression authorizeExpression) {
		this(auth.getAuthority(), auth.getAuthorityName(), authorizeExpression);
	}
	
	public CodeSecurityConfig(String authority, String authorityName, Expression authorizeExpression) {
		super(SecurityUtils.createSecurityExpression(authority));
		this.authority = authority;
		this.authorityName = authorityName;
		this.authorizeExpression = authorizeExpression;
	}
	public String getAuthorityName() {
		return authorityName;
	}
	
	public String getCode(){
		return authority;
	}

	public Expression getAuthorizeExpression() {
		return authorizeExpression;
	}
}
