package org.onetwo.boot.module.oauth2ssoclient.accesstoken;

import org.onetwo.boot.module.oauth2ssoclient.OAuth2SsoClientUserHanlder;
import org.onetwo.boot.module.oauth2ssoclient.OAuth2SsoClientUserInfo;
import org.onetwo.boot.module.oauth2ssoclient.OAuth2SsoContext.RequestOAuth2SsoContext;
import org.onetwo.boot.module.oauth2ssoclient.response.AccessTokenInfo;
import org.onetwo.common.web.utils.WebHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class DefaultSsoClientAccessTokenProvider implements SsoClientAccessTokenProvider {
	
	@Autowired
	private OAuth2SsoClientUserHanlder oauth2SsoClientUserHanlder;

	@Override
	public AccessTokenInfo getAccessToken() {
		RequestOAuth2SsoContext context = new RequestOAuth2SsoContext(WebHolder.getRequest().get());
		OAuth2SsoClientUserInfo userInfo = oauth2SsoClientUserHanlder.getOAuth2SsoClientUserInfo(context, WebHolder.getResponse().get());
		return null;
	}

	@Override
	public AccessTokenInfo refreshAccessToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
