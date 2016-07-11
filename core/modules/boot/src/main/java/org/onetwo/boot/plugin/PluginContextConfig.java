package org.onetwo.boot.plugin;

import org.onetwo.boot.plugin.mvc.PluginWebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginContextConfig {
	
	@Bean
	public PluginWebMvcRegistrations pluginWebMvcRegistrations(){
		return new PluginWebMvcRegistrations();
	}

}
