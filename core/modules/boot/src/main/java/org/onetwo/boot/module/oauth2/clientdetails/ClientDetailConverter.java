package org.onetwo.boot.module.oauth2.clientdetails;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author wayshall
 * <br/>
 */
public interface ClientDetailConverter {
	
	Object convert(OAuth2Authentication oauth2Authentication);

}
