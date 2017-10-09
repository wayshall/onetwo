package org.onetwo.boot.core.jwt;

import org.onetwo.common.web.userdetails.UserDetail;


/**
 * @author wayshall
 * <br/>
 */
public interface JwtTokenService {

	JwtTokenInfo generateToken(UserDetail userDetail);
	JwtTokenInfo generateToken(JwtUserDetail userDetail);

	<T extends UserDetail> T createUserDetail(String token, Class<T> parameterType);
	JwtUserDetail createUserDetail(String token);

}