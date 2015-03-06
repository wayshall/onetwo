package org.onetwo.plugins.permission.service;

import java.util.List;

import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;

public interface PluginPermissionManager extends PermissionManager {

	<T> T findById(Class<?> clazz, Long id);

	List<? extends IMenu> findAppMenus(Class<?> clazz, String appCode);
	public List<? extends IPermission> findAppPermissions(Class<?> clazz, String appCode);
	
	public List<? extends IPermission> findPermissionByCodes(Class<?> clazz, String appCode, String[] permissionCodes);
}