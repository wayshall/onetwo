package org.onetwo.cloud.zuul.limiter;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.InvokeLimiter;
import org.onetwo.boot.limiter.LimiterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;







/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(ZuulLimiterProperties.class)
@ConditionalOnProperty(name=ZuulLimiterProperties.ENABLED_KEY)
public class LimiterConfiguration {

	@Autowired
	private ZuulLimiterProperties limiterProperties;
	
	@Bean
	public PreLimiterZuulFilter preLimiterZuulFilter(LimiterManager limiterManager){
		PreLimiterZuulFilter limiter = new PreLimiterZuulFilter();
		List<InvokeLimiter> preLimiter = Lists.newArrayList();
		preLimiter.addAll(limiterManager.findLimiters(InvokeType.BEFORE));
		preLimiter.addAll(limiterManager.findLimiters(InvokeType.ALL));
		limiter.setLimiters(preLimiter);
		return limiter;
	}
	
	@Bean
	public PostLimiterZuulFilter postLimiterZuulFilter(LimiterManager limiterManager){
		PostLimiterZuulFilter limiter = new PostLimiterZuulFilter();
		List<InvokeLimiter> postLimiter = Lists.newArrayList();
		postLimiter.addAll(limiterManager.findLimiters(InvokeType.AFTER));
		postLimiter.addAll(limiterManager.findLimiters(InvokeType.ALL));
		limiter.setLimiters(postLimiter);
		return limiter;
	}
	
	@Bean
	public LimiterManager LimiterManager(){
		LimiterManager lm = new LimiterManager();
		lm.setLimiterConfigs(new ArrayList<>(limiterProperties.getPolicies().values()));
		lm.buildLimiter();
		return lm;
	}
}
