package org.onetwo.boot.core.jwt;

import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.ext.security.jwt.JwtUserDetail;


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