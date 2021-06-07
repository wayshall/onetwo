package org.onetwo.boot.module.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(SessionProperties.class)
@ConditionalOnClass(HttpSessionIdResolver.class)
@EnableRedisHttpSession
public class BootSpringSessionConfiguration {
	
	@Autowired
	private SessionProperties sessionProperties;
	
	@Bean
	@ConditionalOnProperty(name=SessionProperties.STRATEGY_KEY, havingValue=SessionProperties.STRATEGE_CUSTOMIZABLE)
	public HttpSessionIdResolver headerFirst() {
		CustomizableHttpSessionStrategy strategy = new CustomizableHttpSessionStrategy();
		strategy.setStrategyHeaderName(sessionProperties.getStrategyHeaderName());
		strategy.setTokenHeaderName(sessionProperties.getTokenHeaderName());
		return strategy;
	}
	
	// 修复spring bean的session作用域
//	@Configuration
//	public class RequestContextFilterConfiguration {
//
//	    @Bean
//	    @ConditionalOnMissingBean(RequestContextFilter.class)
//	    public RequestContextFilter requestContextFilter() {
//	        return new RequestContextFilter();
//	    }
//
//	    @Bean
//	    public FilterRegistrationBean requestContextFilterChainRegistration(@Qualifier("requestContextFilter") Filter requestContextFilter) {
//	        FilterRegistrationBean registration = new FilterRegistrationBean(requestContextFilter);
//	        registration.setOrder(SessionRepositoryFilter.DEFAULT_ORDER + 1);
//	        registration.setName("requestContextFilter");
//	        return registration;
//	    }
//	}
}
