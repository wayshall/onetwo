package org.onetwo.plugins.permission;


public interface MenuInfoable {
	
	public Class<?> getRootMenuClass();
	
	public <T> Class<T> getIPermissionClass();
	
//	public IPermission createPermission(PermissionType type);
	
	public <T> Class<T> getIMenuClass();
	
	public <T> Class<T> getIFunctionClass();
	
}
