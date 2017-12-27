package org.onetwo.boot.module.oauth2.clientdetails;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultClientDetailConverter implements ClientDetailConverter {

	@Override
	public Object convert(OAuth2Authentication authentication) {
		ClientDetails details = new ClientDetails(authentication.getOAuth2Request().getClientId());
		return details;
	}
	
	

}
