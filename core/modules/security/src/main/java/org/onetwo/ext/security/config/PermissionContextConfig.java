package org.onetwo.ext.security.config;

import org.onetwo.ext.permission.PermissionConfigAdapter;
import org.onetwo.ext.permission.PermissionHandlerMappingListener;
import org.onetwo.ext.permission.PermissionManager;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.onetwo.ext.permission.service.impl.DefaultMenuItemRepository;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 菜单权限管理
 * 需要：
 * 实现 {@linkplain PermissionConfigAdapter} 接口
 * 实现 {@linkplain PermissionManager} 接口
 * @author way
 *
 */
@Configuration
public class PermissionContextConfig {
	
	@Autowired
	protected SecurityConfig securityConfig;
	
	public PermissionContextConfig(){
	}
	
	/*@Bean
	@Autowired
	public <T extends DefaultIPermission<T>> DefaultMenuInfoParser<T> menuInfoParser(PermissionConfig<T> permissionConfig){
		DefaultMenuInfoParser<T> parser = new DefaultMenuInfoParser<T>(permissionConfig);
		return parser;
	}*/
	
	@Bean
	public MenuItemRepository<PermisstionTreeModel> menuItemRepository(){
		DefaultMenuItemRepository menuItemRepository = new DefaultMenuItemRepository();
		return menuItemRepository;
	}
	
	@Bean
	public PermissionHandlerMappingListener permissionHandlerMappingListener(){
		PermissionHandlerMappingListener listener = new PermissionHandlerMappingListener();
		listener.setSyncPermissionData(securityConfig.getSyncPermissionData());
		return listener;
	}
	

}
