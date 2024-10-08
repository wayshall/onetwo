package org.onetwo.cloud.eureka.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.server.EurekaController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.appinfo.ApplicationInfoManager;

/**
 * @author wayshall
 * <br/>
 */
//@EnableConfigurationProperties({BootJfishCloudConfig.class})
@Configuration
@ConditionalOnClass(EurekaController.class)
public class ExtEurekaServerConfiguration {

	@Autowired
	private ApplicationInfoManager applicationInfoManager;

	/****
	 * eureka.dashboard.enabled: false
	 * @see #{EurekaServerAutoConfiguration#eurekaController}
	 * @return
	 */
	@Bean
//	@ConditionalOnProperty(prefix = "eureka.dashboard", name = "admin", matchIfMissing=true)
	@ConditionalOnMissingBean(EurekaController.class)
	public ExtEurekaController eurekaController() {
		return new ExtEurekaController(this.applicationInfoManager);
	}
	
	public ExtEurekaServerConfiguration(){
	}
}
