package org.onetwo.boot.module.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(SessionProperties.class)
public class BootSpringSessionConfiguration {
	
	@Autowired
	private SessionProperties sessionProperties;
	
	@Bean
	@ConditionalOnProperty(name=SessionProperties.STRATEGY_KEY, havingValue="headerFirst")
	public HttpSessionStrategy headerFirst() {
		HeaderFirstHttpSessionStrategy strategy = new HeaderFirstHttpSessionStrategy();
		strategy.setStrategyHeaderName(sessionProperties.getStrategyHeaderName());
		strategy.setTokenHeaderName(sessionProperties.getTokenHeaderName());
		return strategy;
	}
}
