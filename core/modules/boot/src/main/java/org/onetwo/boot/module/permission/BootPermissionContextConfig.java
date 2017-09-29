package org.onetwo.boot.module.permission;

import org.onetwo.ext.permission.PermissionConfigAdapter;
import org.onetwo.ext.permission.PermissionHandlerMappingListener;
import org.onetwo.ext.permission.PermissionManager;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.onetwo.ext.security.config.PermissionContextConfig;
import org.onetwo.ext.security.metadata.JdbcSecurityMetadataSourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/***
 * 菜单权限管理
 * 需要：
 * 实现 {@linkplain PermissionConfigAdapter} 接口
 * 实现 {@linkplain PermissionManager} 接口
 * @author way
 *
 */
@Configuration
//@ConditionalOnBean({PermissionConfig.class})
@ConditionalOnClass(JdbcDaoSupport.class)
public class BootPermissionContextConfig extends PermissionContextConfig {
	
	
	public BootPermissionContextConfig(){
	}
	
	/*@Bean
	@Autowired
	public <T extends DefaultIPermission<T>> DefaultMenuInfoParser<T> menuInfoParser(PermissionConfig<T> permissionConfig){
		return super.menuInfoParser(permissionConfig);
	}*/
	
	@Bean
//	@ConditionalOnBean(AdminController.class)
	@ConditionalOnMissingBean(MenuItemRepository.class)
	@ConditionalOnBean(JdbcSecurityMetadataSourceBuilder.class)
	public MenuItemRepository<PermisstionTreeModel> menuItemRepository(){
		return super.menuItemRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(value=PermissionHandlerMappingListener.class)
	@ConditionalOnBean(JdbcSecurityMetadataSourceBuilder.class)
	public PermissionHandlerMappingListener permissionHandlerMappingListener(){
		return super.permissionHandlerMappingListener();
	}
	

}
