package org.onetwo.boot.module.dbm;

import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@ConfigurationProperties(prefix=BootDbmConfig.PREFIX)
@Data
@EqualsAndHashCode(callSuper=true)
public class BootDbmConfig extends DefaultDbmConfig {
	
	public static final String PREFIX = "jfish.dbm";
//	public static final String STATIS_ENABLED_KEY = PREFIX + ".web.statisController.enabled";
	
	boolean statisController;

}
