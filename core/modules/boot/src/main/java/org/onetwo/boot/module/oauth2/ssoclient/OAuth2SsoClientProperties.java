package org.onetwo.boot.module.oauth2.ssoclient;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.onetwo.common.apiclient.impl.RestExecutorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @see OAuth2ClientProperties security.oauth2.client.clientId
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(OAuth2SsoClientProperties.CONFIG_PREFIX)
public class OAuth2SsoClientProperties {

	/***
	 * jfish.oauth2.ssoclient
	 */
	public static final String CONFIG_PREFIX = JFishOauth2Properties.CONFIG_PREFIX + ".ssoclient";
	
	public static final String ENABLED_TOKEN_INFO_CUSTOM = CONFIG_PREFIX + ".tokenInfo.enabled";
	
	RestExecutorConfig resttemplate = new RestExecutorConfig(60_000, 60_000, 60_000);
	
	boolean stateMandatory = true;
	
	String loginPath = "/oauth2Login*";

}
