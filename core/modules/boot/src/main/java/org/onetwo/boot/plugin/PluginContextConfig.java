package org.onetwo.boot.plugin;

import org.onetwo.boot.plugin.core.DefaultPluginsManager;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.ftl.WebFtlsContextConfig;
import org.onetwo.boot.plugin.mvc.PluginWebMvcRegistrations;
import org.onetwo.boot.plugin.mvc.interceptor.PluginContextInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebFtlsContextConfig.class})
public class PluginContextConfig {
	
	public PluginContextConfig(){
		System.out.println("init PluginContextConfig");
	}
	
	@Bean
	public PluginWebMvcRegistrations pluginWebMvcRegistrations(){
		return new PluginWebMvcRegistrations();
	}
	
	@Bean
	public PluginManager pluginsManager(){
		return new DefaultPluginsManager();
	}
	
	@Bean
	public PluginContextInterceptor pluginContextInterceptor(){
		return new PluginContextInterceptor();
	}

}
