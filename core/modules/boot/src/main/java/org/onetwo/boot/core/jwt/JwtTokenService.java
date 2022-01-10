package org.onetwo.boot.core.jwt;

import org.onetwo.common.web.userdetails.GenericUserDetail;


/**
 * @author wayshall
 * <br/>
 */
public interface JwtTokenService {

	JwtTokenInfo generateToken(GenericUserDetail<?> userDetail);
	JwtTokenInfo generateToken(JwtUserDetail userDetail);

	<T extends GenericUserDetail<?>> T createUserDetail(String token, Class<T> parameterType);
	JwtUserDetail createUserDetail(String token);

}