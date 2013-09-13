package org.onetwo.plugins.permission;


public interface MenuInfoable {

	public Class<?> getRootMenuClass();
	public String[] getChildMenuPackages();
	
	public Class<?> getIPermissionClass();
	
//	public IPermission createPermission(PermissionType type);
	
	public Class<?> getIMenuClass();
	
	public Class<?> getIFunctionClass();
	
}
