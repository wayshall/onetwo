package org.onetwo.ext.permission;

import java.util.List;

import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.entity.DefaultIPermission;

public interface PermissionManager<P extends DefaultIPermission<P>> {

	void build();

	P getPermission(Class<?> permClass);
	/*
	P getDatabaseRootMenu();

	P getDatabaseMenuNode(Class<P> clazz);*/

	public P getMemoryRootMenu();
	
	/****
	 * 同步菜单
	 */
	void syncMenuToDatabase();

	P findByCode(String code);

	public String parseCode(Class<?> permClass);

	List<P> findAppMenus(String appCode);
	List<P> findUserAppMenus(String appCode, UserDetail userDetail);
	/*public List<P> findAppPermissions(String appCode);
	
	public List<P> findPermissionByCodes(String appCode, String[] permissionCodes);*/
	
	
}