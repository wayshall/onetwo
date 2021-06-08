package org.onetwo.boot.module.session;

import java.util.stream.Stream;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(prefix=SessionProperties.PREFIX)
public class SessionProperties {
	
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".session";
	public static final String STRATEGE_CUSTOMIZABLE = "customizable";
	public static final String STRATEGY_KEY = PREFIX + ".strategy";
	
	private String strategyHeaderName = "x-session-strategy";
	private String tokenHeaderName = "x-auth-token";
	
	public static enum SessionStrategies {
		COOKIE,
		HEADER;
		

		public static SessionStrategies of(String name){
			return Stream.of(values()).filter(s->s.name().equals(name))
										.findAny()
										.orElse(COOKIE);
		}
	}
	
	public String getTokenHeaderName() {
		return tokenHeaderName;
	}

}
