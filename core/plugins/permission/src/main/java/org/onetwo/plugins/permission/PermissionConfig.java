package org.onetwo.plugins.permission;

import org.onetwo.common.utils.ReflectUtils;



public interface PermissionConfig {

	default public String getAppCode(){
		String appCode = (String)ReflectUtils.getStaticFieldValue(getRootMenuClass(), "appCode");
		return appCode;
	}
	
	public Class<?> getRootMenuClass();
	public String[] getChildMenuPackages();
	
	public Class<?> getIPermissionClass();
	
//	public IPermission createPermission(PermissionType type);
	
	public Class<?> getIMenuClass();
	
	public Class<?> getIFunctionClass();
	
}
