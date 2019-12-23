package org.onetwo.cloud.hystrix;

import org.onetwo.cloud.core.BootJfishCloudConfig;
import org.onetwo.cloud.env.AuthEnvsConfiguration;
import org.onetwo.cloud.hystrix.SpringMvcRequestContextConfiguration.SpringMvcRequestContextCondition;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.netflix.hystrix.Hystrix;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@Conditional(SpringMvcRequestContextCondition.class)
@Import(AuthEnvsConfiguration.class)
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

		@ConditionalOnProperty(name = BootJfishCloudConfig.HYSTRIX_SHARE_REQUEST_CONTEXT, matchIfMissing=true)
		static class ShareRequestContext {
		}

		/***
		 * 以前默认为true，Dalston版本后修改了默认值
		 * 参看：HystrixFeignConfiguration
		 * https://github.com/spring-cloud/spring-cloud-netflix/issues/1277
		 * @author wayshall
		 *
		 */
		/*@ConditionalOnProperty(name = "feign.hystrix.enabled")
		static class feignHystrixConfig {
		}*/

		@ConditionalOnClass(Hystrix.class)
		static class OnHystrix {
		}
	}

}
