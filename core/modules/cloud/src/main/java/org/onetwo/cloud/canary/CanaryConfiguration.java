package org.onetwo.cloud.canary;

import org.onetwo.cloud.hystrix.SpringMvcRequestContextConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({CanaryProperties.class})
public class CanaryConfiguration implements InitializingBean {
	

	@Override
	public void afterPropertiesSet() throws Exception {
		/*if(keepHeaderRequestInterceptor!=null){
			Set<String> keepHeaders = new HashSet<>(keepHeaderRequestInterceptor.getKeepHeaders());
			keepHeaders.add(CanaryConsts.HEADER_CLIENT_TAG);
			this.keepHeaderRequestInterceptor.setKeepHeaders(ImmutableSet.copyOf(keepHeaders));;
		}*/
	}
	

}

@Configuration
class CanaryRibbonConfiguration {
	
	@Bean
	public CanaryRule canaryRule(){
		CanaryRule canaryRule = new CanaryRule();
		return canaryRule;
	}
	
}