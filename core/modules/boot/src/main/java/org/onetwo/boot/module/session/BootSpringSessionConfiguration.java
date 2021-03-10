package org.onetwo.boot.module.session;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.RequestContextFilter;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(SessionProperties.class)
@ConditionalOnClass(HttpSessionStrategy.class)
public class BootSpringSessionConfiguration {
	
	@Autowired
	private SessionProperties sessionProperties;
	
	@Bean
	@ConditionalOnProperty(name=SessionProperties.STRATEGY_KEY, havingValue=SessionProperties.STRATEGE_CUSTOMIZABLE)
	public HttpSessionStrategy headerFirst() {
		CustomizableHttpSessionStrategy strategy = new CustomizableHttpSessionStrategy();
		strategy.setStrategyHeaderName(sessionProperties.getStrategyHeaderName());
		strategy.setTokenHeaderName(sessionProperties.getTokenHeaderName());
		return strategy;
	}
	
	@Configuration
	public class RequestContextFilterConfiguration {

	    @Bean
	    @ConditionalOnMissingBean(RequestContextFilter.class)
	    public RequestContextFilter requestContextFilter() {
	        return new RequestContextFilter();
	    }

	    @Bean
	    public FilterRegistrationBean requestContextFilterChainRegistration(@Qualifier("requestContextFilter") Filter requestContextFilter) {
	        FilterRegistrationBean registration = new FilterRegistrationBean(requestContextFilter);
	        registration.setOrder(SessionRepositoryFilter.DEFAULT_ORDER + 1);
	        registration.setName("requestContextFilter");
	        return registration;
	    }
	}
}
