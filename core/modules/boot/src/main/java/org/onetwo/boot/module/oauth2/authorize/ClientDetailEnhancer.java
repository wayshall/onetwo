package org.onetwo.boot.module.oauth2.authorize;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * 示例，可通过实现TokenEnhancer接口增加额外的扩展字段
 * @author wayshall
 * <br/>
 */
public class ClientDetailEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		accessToken.getAdditionalInformation().put("customerKey", "");
		return accessToken;
	}

}
