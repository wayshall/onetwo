package org.onetwo.boot.module.oauth2.restclient;

import org.onetwo.common.apiclient.simple.SimpleApiClentRegistrar;

/**
 * @author weishao zeng
 * <br/>
 */
public class OAuthRestClientRegistar extends SimpleApiClentRegistrar {

	public OAuthRestClientRegistar() {
		super(OAuth2TokenClient.class);
	}

}

