package org.onetwo.boot.module.oauth2.authorize;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author wayshall
 * <br/>
 */
public class DelegateAuthenticationManager implements AuthenticationManager {
	
	private AuthenticationManager delegate;
	

	public DelegateAuthenticationManager(AuthenticationManager delegate) {
		super();
		this.delegate = delegate;
	}


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		return delegate.authenticate(authen);
	}

}
