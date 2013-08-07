package org.onetwo.plugins.permission;

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
	
}
