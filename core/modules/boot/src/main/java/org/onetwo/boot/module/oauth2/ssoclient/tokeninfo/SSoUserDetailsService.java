package org.onetwo.boot.module.oauth2.ssoclient.tokeninfo;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author weishao zeng
 * <br/>
 */
public interface SSoUserDetailsService {
	
	UserDetails loadUserByOAuth2User(Map<String, ?> map);

}
