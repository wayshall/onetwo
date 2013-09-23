package org.onetwo.plugins.permission;



public interface PermissionConfig {
	
	public Class<?> getRootMenuClass();
	public String[] getChildMenuPackages();
	
	public Class<?> getIPermissionClass();
	
//	public IPermission createPermission(PermissionType type);
	
	public Class<?> getIMenuClass();
	
	public Class<?> getIFunctionClass();
	
}
