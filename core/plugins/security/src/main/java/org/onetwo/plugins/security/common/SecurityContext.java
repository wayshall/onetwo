package org.onetwo.plugins.security.common;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.SessionStorer;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan(basePackageClasses = { SpringAuthenticationInvocation.class })
public class SecurityContext implements InitializingBean {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(SecurityPluginUtils.existServerConfig() || SecurityPluginUtils.existClientConfig()){
			SsoConfigInitializer init = new SsoConfigInitializer();
			SsoConfig ssoConfig = SpringUtils.getBean(applicationContext, SsoConfig.class);
			init.setSsoConfig(ssoConfig);
			SpringUtils.registerSingleton(applicationContext, "ssoConfigInitializer", init);
		}
	}

	
	@Bean
	public SessionStorer sessionStorer(){
		if(SecurityPluginUtils.existServerConfig()){
			return new MemorySessionStorer();
		}else{
			return new HttpSessionStorer();
		}
	}
	
}
