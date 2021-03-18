package org.onetwo.boot.module.oauth2.ssoclient.event;

import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class AccessTokenObtainedEvent extends ApplicationEvent {
	
	final private OAuth2AccessToken accessToken;
	final private UserDetail userDetails;
	
	public AccessTokenObtainedEvent(Object source, UserDetail userDetails, OAuth2AccessToken accessToken) {
		super(source);
		this.userDetails = userDetails;
		this.accessToken = accessToken;
	}

	public OAuth2AccessToken getAccessToken() {
		return accessToken;
	}

	public UserDetail getUserDetails() {
		return userDetails;
	}


}
