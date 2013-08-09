package org.onetwo.plugins.permission;

import org.onetwo.plugins.permission.service.PermissionConfigBuilder;
import org.onetwo.plugins.permission.service.PermissionManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PermissionPluginContext {
	
	@Bean
	public PermissionHandlerMappingListener jresourceHandlerMappingListener(){
		PermissionHandlerMappingListener l = new PermissionHandlerMappingListener();
		return l;
	}
	
	@Bean
	public PermissionManagerImpl permissionManagerImpl(){
		return new PermissionManagerImpl();
	}
	
	@Bean
	public MenuInfoParser menuInfoParser(){
		return new DefaultMenuInfoParser();
	}
	
	@Bean
	public PermissionConfigBuilder permissionConfigBuilder(){
		return new PermissionConfigBuilder();
	}
	
}
