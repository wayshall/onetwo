package org.onetwo.boot.module.oauth2ssoclient;
/**
 * @author weishao zeng
 * <br/>
 */

import org.springframework.web.client.RestOperations;

public interface OAuth2RestClient extends RestOperations {
	
	
	OAuth2SsoClientUserInfo getUserInfo(String code);

}
