package org.onetwo.boot.module.oauth2.clientdetails;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author wayshall
 * <br/>
 */
public interface ClientDetailConverter {
	
	Object convert(OAuth2AccessToken accessToken, OAuth2Authentication oauth2Authentication);

}
