package org.onetwo.plugins.permission.service;

import java.util.List;

import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;

public interface PermissionManager {

	void build();

	IPermission getPermission(Class<?> permClass);

	IMenu getDatabaseRootMenu();

	IMenu getDatabaseMenuNode(Class<?> clazz);

	/****
	 * 同步菜单
	 */
	void syncMenuToDatabase();

	<T> T findById(Long id);

	public String parseCode(Class<?> permClass);
	
	List<? extends IMenu> findAppMenus(String appCode);
	public List<? extends IPermission> findAppPermissions(String appCode);
	
	public List<? extends IPermission> findPermissionByCodes(String appCode, String[] permissionCodes);
	
	
}