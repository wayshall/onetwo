package org.onetwo.plugins.admin.utils;

import org.onetwo.ext.permission.PermissionConfigAdapter;
import org.onetwo.plugins.admin.entity.AdminPermission;

/****
 * 简化PermissionConfig配置，提供RootMenuClassProvider接口
 * @author way
 *
 */
public class WebAdminPermissionConfig extends PermissionConfigAdapter<AdminPermission>{
	
	private RootMenuClassProvider rootMenuClassProvider;

	@Override
	public Class<?> getRootMenuClass() {
		return rootMenuClassProvider.rootMenuClass();
	}

	@Override
	public Class<AdminPermission> getIPermissionClass() {
		return AdminPermission.class;
	}
	
	public static interface RootMenuClassProvider {
		Class<?> rootMenuClass();
	}

	public void setRootMenuClassProvider(RootMenuClassProvider rootMenuClassProvider) {
		this.rootMenuClassProvider = rootMenuClassProvider;
	}

}
