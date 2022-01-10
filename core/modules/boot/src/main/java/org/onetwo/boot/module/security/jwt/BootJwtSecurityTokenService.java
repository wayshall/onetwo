package org.onetwo.boot.module.security.jwt;

import java.io.Serializable;
import java.util.Collection;

import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.ext.security.jwt.DefaultJwtSecurityTokenService;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;

/**
 * @author weishao zeng
 * <br/>
 */
public class BootJwtSecurityTokenService extends DefaultJwtSecurityTokenService {
	
	@Override
	protected GenericUserDetail<?> createUserDetailForAuthentication(Serializable userId, String username, 
			Collection<? extends GrantedAuthority> authorities, Claims claims) {
		SecurityJwtUserDetail user = new SecurityJwtUserDetail(userId, username, "N/A", authorities);
		user.setProperties(toMap(claims));
		return user;
	}
	
}
