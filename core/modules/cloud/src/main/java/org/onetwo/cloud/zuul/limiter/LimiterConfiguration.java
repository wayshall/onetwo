package org.onetwo.cloud.zuul.limiter;

import org.onetwo.boot.limiter.LimiterRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(ZuulLimiterProperties.class)
@ConditionalOnProperty(prefix=ZuulLimiterProperties.ENABLED_KEY)
public class LimiterConfiguration {

	@Autowired
	private ZuulLimiterProperties limiterProperties;
	
	@Bean
	public PreLimiterZuulFilter preLimiterZuulFilter(LimiterRegister limiterRegister){
		PreLimiterZuulFilter limiter = new PreLimiterZuulFilter();
		
		limiterProperties.getPolicies().entrySet().stream().filter(entry->{
			return entry.getValue().get
		}).map(entry->{
			return null;
		});
		
		return limiter;
	}
	
	@Bean
	public PostLimiterZuulFilter postLimiterZuulFilter(){
		PostLimiterZuulFilter limiter = new PostLimiterZuulFilter();
		return limiter;
	}
	
	@Bean
	public LimiterRegister limiterRegister(){
		return new LimiterRegister();
	}
}
