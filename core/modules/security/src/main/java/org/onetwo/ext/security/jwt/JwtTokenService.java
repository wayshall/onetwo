package org.onetwo.ext.security.jwt;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

/**
 * @author wayshall
 * <br/>
 */
public interface JwtTokenService {

	JwtTokenInfo generateToken(Authentication authentication);

	Authentication createAuthentication(String token) throws BadCredentialsException;

}