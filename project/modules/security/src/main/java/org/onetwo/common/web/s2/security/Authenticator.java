package org.onetwo.common.web.s2.security;

import org.onetwo.common.exception.AuthenticationException;


public interface Authenticator {

	public boolean beforeTarget(AuthenticationContext context) throws AuthenticationException;
	
}
