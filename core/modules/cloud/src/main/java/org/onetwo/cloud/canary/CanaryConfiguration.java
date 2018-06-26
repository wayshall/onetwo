package org.onetwo.cloud.canary;

import org.onetwo.cloud.hystrix.SpringMvcRequestContextConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@RibbonClients(defaultConfiguration=CanaryRibbonConfiguration.class)
@Import(SpringMvcRequestContextConfiguration.class)
public class CanaryConfiguration {

}

@Configuration
class CanaryRibbonConfiguration {
	
	@Bean
	public CanaryRule canaryRule(){
		CanaryRule canaryRule = new CanaryRule();
		return canaryRule;
	}
	
}