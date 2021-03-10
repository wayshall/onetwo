package org.onetwo.boot.module.security;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.onetwo.common.apiclient.impl.RestExecutorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(OAuth2SsoClientProperties.CONFIG_PREFIX)
public class OAuth2SsoClientProperties {

	public static final String CONFIG_PREFIX = JFishOauth2Properties.CONFIG_PREFIX + ".ssoclient";
	
	public static final String ENABLED_TOKEN_INFO_CUSTOM = CONFIG_PREFIX + ".tokenInfo.enabled";
	
	RestExecutorConfig resttemplate = new RestExecutorConfig();
	
	boolean stateMandatory = true;
	
	String loginPath = "/oauth2Login*";

}
