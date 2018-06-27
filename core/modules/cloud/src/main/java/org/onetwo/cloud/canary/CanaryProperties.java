package org.onetwo.cloud.canary;

import lombok.Data;

import org.onetwo.cloud.canary.CanaryUtils.CanaryMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(prefix=CanaryProperties.PREFIX)
@Data
public class CanaryProperties {
	
	public static final String PREFIX = "jfish.cloud.cannary";
	
	CanaryMode defaultMode = CanaryMode.DISABLED;

}
