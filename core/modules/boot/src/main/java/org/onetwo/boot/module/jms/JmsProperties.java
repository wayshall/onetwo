package org.onetwo.boot.module.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(value=JmsProperties.PREFIX_KEY)
@Data
public class JmsProperties {
	public static final String PREFIX_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".jms";
	
	/***
	 * jfish.jms.converter
	 */
//	public static final String CONVERTER_KEY = PREFIX_KEY+".converter";
	
}
