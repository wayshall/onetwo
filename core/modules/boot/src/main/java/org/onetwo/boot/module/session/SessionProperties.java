package org.onetwo.boot.module.session;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(prefix=SessionProperties.PREFIX)
public class SessionProperties {
	
	public static final String PREFIX = "jfish.session";
	public static final String STRATEGY_KEY = PREFIX + ".strategy";
	
	private String strategyHeaderName = "x-session-strategy";
	private String tokenHeaderName = "x-auth-token";
	
	public static enum SessionStrategies {
		COOKIE,
		HEADER
	}

}
