package org.onetwo.plugins.permission.service;

import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;

public interface PermissionManager {

	void build();

	IPermission getPermission(Class<?> permClass);

	<T extends IMenu> T getDatabaseRootMenu();

	<T extends IMenu> T getDatabaseMenuNode(Class<?> clazz);

	/****
	 * 同步菜单
	 */
	void syncMenuToDatabase();

	<T> T findById(Long id);

}