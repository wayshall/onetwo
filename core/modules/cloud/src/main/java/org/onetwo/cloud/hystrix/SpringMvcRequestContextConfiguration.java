package org.onetwo.cloud.hystrix;

import javax.annotation.PostConstruct;

import org.onetwo.cloud.hystrix.SpringMvcRequestContextConfiguration.SpringMvcRequestContextCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@Conditional(SpringMvcRequestContextCondition.class)
public class SpringMvcRequestContextConfiguration {
	@Autowired(required = false)
	private HystrixConcurrencyStrategy existingConcurrencyStrategy;

	@PostConstruct
	public void init() {
	}
	
	@Bean
	public RequestContextConcurrencyStrategy requestContextConcurrencyStrategy(){
		return new RequestContextConcurrencyStrategy();
	}
	
	/***
	 * 所有内嵌类匹配的时候才匹配
	 * @author wayshall
	 *
	 */
	public static class SpringMvcRequestContextCondition extends AllNestedConditions {

		public SpringMvcRequestContextCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnProperty(name = "jfish.cloud.hystrix.shareRequestContext")
		static class ShareRequestContext {
		}
	}

}
