package org.onetwo.ext.permission;

import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.api.PermissionConfig;

public class SimplePermissionConfig<P extends IPermission> implements PermissionConfig<P> {
	
	private Class<?> rootMenuClass;
	private Class<P> permissionClass;
	

	public SimplePermissionConfig(Class<?> rootMenuClass, Class<P> permissionClass) {
		super();
		this.rootMenuClass = rootMenuClass;
		this.permissionClass = permissionClass;
	}

	@Override
	public Class<?> getRootMenuClass() {
		return rootMenuClass;
	}

	@Override
	public Class<P> getIPermissionClass() {
		return permissionClass;
	}
	
}
