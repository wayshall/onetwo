package org.onetwo.boot.module.oauth2.ssoclient.tokeninfo;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

/**
 * @author weishao zeng
 * <br/>
 */
public class CustomSsoClientUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
	
	private SSoUserDetailsService ssoUserDetailService;
	
	public Authentication extractAuthentication(Map<String, ?> map) {
		if (ssoUserDetailService==null) {
			return super.extractAuthentication(map);
		}
		Object userDetail = ssoUserDetailService.loadUserByOAuth2User(map);
		if (userDetail!=null) {
			if (userDetail instanceof UserDetails) {
				UserDetails u = (UserDetails) userDetail;
				return new UsernamePasswordAuthenticationToken(userDetail, "N/A", u.getAuthorities());
			} else {
				return new UsernamePasswordAuthenticationToken(userDetail, "N/A", Collections.emptyList());
			}
		}
		return null;
	}

	public void setSsoUserDetailService(SSoUserDetailsService ssoUserDetailService) {
		this.ssoUserDetailService = ssoUserDetailService;
	}


}
