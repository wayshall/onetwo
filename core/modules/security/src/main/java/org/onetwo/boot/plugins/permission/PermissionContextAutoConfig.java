package org.onetwo.boot.plugins.permission;

import org.onetwo.boot.plugins.permission.entity.AbstractPermission;
import org.onetwo.boot.plugins.permission.entity.PermisstionTreeModel;
import org.onetwo.boot.plugins.permission.parser.DefaultMenuInfoParser;
import org.onetwo.boot.plugins.permission.service.MenuItemRepository;
import org.onetwo.boot.plugins.permission.service.impl.DefaultMenuItemRepository;
import org.onetwo.boot.plugins.permission.web.controller.AdminController;
import org.onetwo.boot.plugins.permission.web.controller.PermissionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 菜单权限管理
 * 需要：
 * 实现 {@linkplain AbstractPermissionConfig} 接口
 * 实现 {@linkplain PermissionManager} 接口
 * @author way
 *
 */
@Configuration
@ConditionalOnBean(AbstractPermissionConfig.class)
public class PermissionContextAutoConfig {
	
	public PermissionContextAutoConfig(){
	}
	
	@Bean
	public PermissionController permissionController(){
		return new PermissionController();
	}
	
	@Bean
	@Autowired
	public <T extends AbstractPermission<T>> DefaultMenuInfoParser<T> menuInfoParser(AbstractPermissionConfig<T> permissionConfig){
		DefaultMenuInfoParser<T> parser = new DefaultMenuInfoParser<T>(permissionConfig);
		return parser;
	}
	
	@Bean
	@ConditionalOnBean(AdminController.class)
	@ConditionalOnMissingBean(MenuItemRepository.class)
	public MenuItemRepository<PermisstionTreeModel> menuItemRepository(){
		DefaultMenuItemRepository menuItemRepository = new DefaultMenuItemRepository();
		return menuItemRepository;
	}
	
	@Bean
	public PermissionHandlerMappingListener permissionHandlerMappingListener(){
		return new PermissionHandlerMappingListener();
	}
	

}
