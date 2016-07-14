package org.onetwo.boot.plugin;

import org.onetwo.boot.plugin.core.DefaultPluginsManager;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.ftl.FtlContextConfig;
import org.onetwo.boot.plugin.mvc.PluginWebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FtlContextConfig.class})
public class PluginContextConfig {
	
	@Bean
	public PluginWebMvcRegistrations pluginWebMvcRegistrations(){
		return new PluginWebMvcRegistrations();
	}
	
	@Bean
	public PluginManager pluginsManager(){
		return new DefaultPluginsManager();
	}

}
