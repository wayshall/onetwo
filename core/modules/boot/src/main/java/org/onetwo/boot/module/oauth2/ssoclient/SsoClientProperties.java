package org.onetwo.boot.module.oauth2.ssoclient;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties
public class SsoClientProperties {

	public static final String CONFIG_PREFIX = JFishOauth2Properties.CONFIG_PREFIX + ".ssoclient";
	
	public static final String ENABLED_TOKEN_INFO_CUSTOM = CONFIG_PREFIX + ".tokenInfo.enabled";

}
