package org.onetwo.boot.module.oauth2ssoclient.accesstoken;

import org.onetwo.boot.module.oauth2ssoclient.response.AccessTokenInfo;

/**
 * @author weishao zeng
 * <br/>
 */
public interface SsoClientAccessTokenProvider {

	AccessTokenInfo getAccessToken();
	AccessTokenInfo refreshAccessToken();
}
