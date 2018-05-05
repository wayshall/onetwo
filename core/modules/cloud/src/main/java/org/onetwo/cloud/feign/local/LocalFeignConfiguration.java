package org.onetwo.cloud.feign.local;

import org.onetwo.cloud.feign.FeignClientPluginContextPathChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
//@AutoConfigureBefore(FeignAutoConfiguration.classS)
public class LocalFeignConfiguration {

	@Bean
	public FeignClientPluginContextPathChecker feignClientPluginContextPathChecker(){
		return new FeignClientPluginContextPathChecker();
	}
	
}
