package org.onetwo.boot.core.web.mvc.log;

import org.onetwo.boot.core.web.mvc.interceptor.LoggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@EnableConfigurationProperties({AccessLogProperties.class})
@ConditionalOnProperty(value=AccessLogProperties.ENABLE_MVC_LOGGER_INTERCEPTOR, matchIfMissing=false, havingValue="true")
@Configuration
public class AccessLogConfiguration {
	
	@Autowired
	private AccessLogProperties accessLogProperties;

	@Bean
	@ConditionalOnMissingBean(LoggerInterceptor.class)
	public LoggerInterceptor loggerInterceptor(){
		LoggerInterceptor interceptor = new LoggerInterceptor();
		interceptor.setPathPatterns(accessLogProperties.getPathPatterns());
		return interceptor;
	}
	
	@Bean
	public AccessLogInterceptorConfigurerAdapter accessLogInterceptorConfigurerAdapter(){
		AccessLogInterceptorConfigurerAdapter adapter = new AccessLogInterceptorConfigurerAdapter();
		return adapter;
	}
}
