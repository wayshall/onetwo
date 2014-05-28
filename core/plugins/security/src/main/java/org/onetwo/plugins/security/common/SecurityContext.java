package org.onetwo.plugins.security.common;

import org.onetwo.common.utils.SessionStorer;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan(basePackageClasses = { SpringAuthenticationInvocation.class })
public class SecurityContext {
	
	@Bean
	public SsoConfigInitializer ssoConfigInitializer(){
		return new SsoConfigInitializer();
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
