package org.onetwo.cloud.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(EnvProperties.PROPERTIES_PREFIX)
@Data
public class EnvProperties {

	public static final String PROPERTIES_PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".cloud.env";
	public static final String ENABLE_KEY = PROPERTIES_PREFIX + ".enabled";
	
	String[] keepHeaders;
}
