package org.onetwo.ext.permission;

import java.util.List;

import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.api.IPermission;

public interface PermissionManager<P extends IPermission> {

	void build();

	P getPermission(Class<?> permClass);
	/*
	P getDatabaseRootMenu();

	P getDatabaseMenuNode(Class<P> clazz);*/

	public List<P> getMemoryRootMenu();
	
	/****
	 * 同步菜单
	 */
	void syncMenuToDatabase();
	
	/***
	 * 刷新security的权限数据（内存）
	 * @author weishao zeng
	 */
	void refreshSecurityMetadataSource();

	P findByCode(String code);

//	public String parseCode(Class<?> permClass);

	List<P> findAppMenus(String appCode);
	List<P> findUserAppMenus(String appCode, UserDetail userDetail);
	List<P> findAppPermissions(String appCode);
	/*
	public List<P> findPermissionByCodes(String appCode, String[] permissionCodes);*/
	
	
}