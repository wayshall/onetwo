package org.onetwo.boot.module.dbm;

import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix=BootDbmConfig.PREFIX)
public class BootDbmConfig extends DefaultDbmConfig {
	
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".dbm";
	public static final String STATIS_ENABLED_KEY = PREFIX + ".web.statisController.enabled";

}
