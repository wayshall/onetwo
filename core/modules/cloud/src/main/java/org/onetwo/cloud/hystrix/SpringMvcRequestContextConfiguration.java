package org.onetwo.cloud.hystrix;

import org.onetwo.cloud.hystrix.SpringMvcRequestContextConfiguration.SpringMvcRequestContextCondition;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.Hystrix;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@Conditional(SpringMvcRequestContextCondition.class)
public class SpringMvcRequestContextConfiguration {

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

		@ConditionalOnClass(Hystrix.class)
		static class OnHystrix {
		}
	}

}
