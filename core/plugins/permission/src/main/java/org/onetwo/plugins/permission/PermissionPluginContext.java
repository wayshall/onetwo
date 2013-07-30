package org.onetwo.plugins.permission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=PermissionPlugin.class)
public class PermissionPluginContext {
	
	@Bean
	public JresourceHandlerMappingListener jresourceHandlerMappingListener(){
		JresourceHandlerMappingListener l = new JresourceHandlerMappingListener();
		return l;
	}
	
	@Bean
	public JresourceManagerImpl jresourceManager(){
		return jresourceHandlerMappingListener().getJresourceManager();
	}
}
