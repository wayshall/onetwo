package org.onetwo.plugins.admin.utils;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.ext.permission.PermissionConfigAdapter;
import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.plugins.admin.entity.AdminPermission;

/****
 * 简化PermissionConfig配置，提供RootMenuClassProvider接口
 * @author way
 *
 */
public class WebAdminPermissionConfig extends PermissionConfigAdapter<AdminPermission>{
	
	private Class<?> rootMenuClass;

	@Override
	public Class<?> getRootMenuClass() {
		return rootMenuClass;
	}

	public void setRootMenuClass(Class<?> rootMenuClass) {
		this.rootMenuClass = rootMenuClass;
	}

	@Override
	public Class<AdminPermission> getIPermissionClass() {
		return AdminPermission.class;
	}
	
	public static interface RootMenuClassProvider {
		Class<?> rootMenuClass();
	}
	
	/****
	 * 增加支持list的接口
	 * @author wayshall
	 *
	 */
	public static interface RootMenuClassListProvider extends RootMenuClassProvider {
		default Class<?> rootMenuClass() {
			return null;
		}
		List<Class<?>> rootMenuClassList();
	}

	/*public void setRootMenuClassProvider(RootMenuClassProvider rootMenuClassProvider) {
		this.rootMenuClassProvider = rootMenuClassProvider;
	}*/

	@SuppressWarnings("serial")
	static public class AdminPermissionConfigListAdapetor extends ArrayList<PermissionConfig<AdminPermission>> {
	}
}
