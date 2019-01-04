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

	public static final String PROPERTIES_PREFIX = "jfish.cloud.env";
	public static final String ENABLE_KEY = PROPERTIES_PREFIX + ".enabled";
	
	String[] keepHeaders;
}
