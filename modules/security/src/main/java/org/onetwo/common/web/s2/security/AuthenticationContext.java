package org.onetwo.common.web.s2.security;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;

public class AuthenticationContext {
	public static final String AUTHENTICATION_CONTEXT_KEY = "___AuthenticationContext___";
	
	public static AuthenticationContext create(AuthenticConfig config, SecurityTarget target){
		return new AuthenticationContext(config, target);
	}
	AuthenticConfig config;
	SecurityTarget target;
	UserDetail authoritable;
	String cookieToken;
	
	private AuthenticationContext(AuthenticConfig config, SecurityTarget target) {
		super();
		this.config = config;
		this.target = target;
		this.authoritable = target.getAuthoritable();
		this.cookieToken = target.getCookieToken();
	}
	public AuthenticConfig getConfig() {
		return config;
	}
	public SecurityTarget getTarget() {
		return target;
	}
	public UserDetail getAuthoritable() {
		return authoritable;
	}
	public String getCookieToken() {
		return cookieToken;
	}
}
