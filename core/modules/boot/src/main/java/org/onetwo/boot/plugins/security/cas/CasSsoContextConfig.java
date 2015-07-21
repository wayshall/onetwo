package org.onetwo.boot.plugins.security.cas;

import org.onetwo.boot.plugins.security.BootSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;

/***
 * TODO
 * @author way
 *
 */
@Configuration
@EnableConfigurationProperties({BootSecurityConfig.class})
public class CasSsoContextConfig {
	
	@Autowired
	private BootSecurityConfig bootSecurityConfig;
	
	@Bean
	public ServiceProperties serviceProperties(){
		ServiceProperties serviceProps = new ServiceProperties();
		serviceProps.setService(bootSecurityConfig.getServiceProperties().getService());
		serviceProps.setSendRenew(bootSecurityConfig.getServiceProperties().isSendRenew());
		return serviceProps;
	}

}
