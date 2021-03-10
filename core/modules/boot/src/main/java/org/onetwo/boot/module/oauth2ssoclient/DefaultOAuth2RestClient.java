package org.onetwo.boot.module.oauth2ssoclient;

import org.onetwo.boot.module.oauth2ssoclient.response.OAuth2AccessTokenResponse;
import org.springframework.web.client.RestTemplate;

/**
 * @author weishao zeng
 * <br/>
 */
public class DefaultOAuth2RestClient extends RestTemplate implements OAuth2RestClient {


	@Override
	public OAuth2SsoClientUserInfo getUserInfo(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
