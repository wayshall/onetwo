package org.onetwo.boot.plugin;

import org.onetwo.boot.plugin.core.DefaultPluginsManager;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.ftl.WebFtlsContextConfig;
import org.onetwo.boot.plugin.mvc.BootPluginRequestMappingCombiner;
import org.onetwo.boot.plugin.mvc.interceptor.PluginContextInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebFtlsContextConfig.class})
public class PluginContextConfig {
	
	public PluginContextConfig(){
	}
	
	@Bean
	public BootPluginRequestMappingCombiner bootPluginRequestMappingCombiner(){
		return new BootPluginRequestMappingCombiner();
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
