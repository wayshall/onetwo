package org.onetwo.cloud.canary;

import lombok.Data;

import org.onetwo.cloud.canary.CanaryUtils.CanaryMode;
import org.onetwo.cloud.canary.CanaryUtils.CanaryServerNotFoundActions;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(prefix=CanaryProperties.PREFIX)
@Data
public class CanaryProperties {
	
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".cloud.cannary";
	
	CanaryMode defaultMode = CanaryMode.DISABLED;
	CanaryServerNotFoundActions serverNotFoundAction = CanaryServerNotFoundActions.SMOOTHNESS;

}
