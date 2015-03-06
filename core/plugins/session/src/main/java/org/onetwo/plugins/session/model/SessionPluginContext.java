package org.onetwo.plugins.session.model;

import org.onetwo.plugins.session.SessionPlugin;
import org.onetwo.plugins.session.SessionPluginConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan(basePackageClasses=SessionPluginContext.class)
public class SessionPluginContext {
	
	/*@Bean
	public SpringSessionInitializer springSessionInitializer(){
		return new SpringSessionInitializer();
	}*/
	
	@Bean
	public SessionPluginConfig sessionPluginConfig(){
		return SessionPlugin.getInstance().getConfig();
	}
}
