package org.onetwo.boot.module.oauth2.ssoclient.tokeninfo;

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
		UserDetails userDetail = ssoUserDetailService.loadUserByOAuth2User(map);
		if (userDetail!=null) {
			return new UsernamePasswordAuthenticationToken(userDetail, "N/A", userDetail.getAuthorities());
		}
		return null;
	}

	public void setSsoUserDetailService(SSoUserDetailsService ssoUserDetailService) {
		this.ssoUserDetailService = ssoUserDetailService;
	}


}
