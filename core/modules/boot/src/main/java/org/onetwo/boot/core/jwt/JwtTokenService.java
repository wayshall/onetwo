package org.onetwo.boot.core.jwt;


/**
 * @author wayshall
 * <br/>
 */
public interface JwtTokenService {

	JwtTokenInfo generateToken(JwtUserDetail userDetail);

	JwtUserDetail createUserDetail(String token);

}