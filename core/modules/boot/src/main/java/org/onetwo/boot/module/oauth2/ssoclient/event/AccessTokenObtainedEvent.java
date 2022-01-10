package org.onetwo.boot.module.oauth2.ssoclient.event;

import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class AccessTokenObtainedEvent extends ApplicationEvent {
	
	final private OAuth2AccessToken accessToken;
	final private GenericUserDetail<?> userDetails;
	
	public AccessTokenObtainedEvent(Object source, GenericUserDetail<?> userDetails, OAuth2AccessToken accessToken) {
		super(source);
		this.userDetails = userDetails;
		this.accessToken = accessToken;
	}

	public OAuth2AccessToken getAccessToken() {
		return accessToken;
	}

	public GenericUserDetail<?> getUserDetails() {
		return userDetails;
	}


}
