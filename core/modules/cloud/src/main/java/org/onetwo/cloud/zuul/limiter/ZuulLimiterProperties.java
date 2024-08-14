package org.onetwo.cloud.zuul.limiter;

import java.util.Map;

import lombok.Data;

import org.onetwo.boot.limiter.LimiterCreator.LimiterConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(ZuulLimiterProperties.PREFIX)
@Data
public class ZuulLimiterProperties implements InitializingBean{
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".cloud.zuul.limiter";
	public static final String ENABLED_KEY = PREFIX + ".enabled";
	
	Map<String, LimiterConfig> policies = Maps.newHashMap();

	@Override
	public void afterPropertiesSet() throws Exception {
		policies.forEach((key, value)->{
			value.setKey(key);
		});
	}
	
}
