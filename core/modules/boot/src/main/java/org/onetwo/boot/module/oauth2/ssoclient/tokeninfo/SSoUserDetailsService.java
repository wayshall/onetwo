package org.onetwo.boot.module.oauth2.ssoclient.tokeninfo;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author weishao zeng
 * <br/>
 */
public interface SSoUserDetailsService {
	
	UserDetails loadUserByUsername(String username);

}
