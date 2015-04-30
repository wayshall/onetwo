package org.onetwo.plugins.permission;

import java.lang.reflect.Field;

import org.onetwo.common.utils.ReflectUtils;



public interface PermissionConfig {

	default public String getAppCode(){
		String appCode = "";
		Field appCodeField = ReflectUtils.findField(getRootMenuClass(), "appCode");
		if(appCodeField!=null){
			appCode = (String)ReflectUtils.getFieldValue(appCodeField, getRootMenuClass(), false);
		}else{
			appCode = getRootMenuClass().getSimpleName();
		}
		return appCode;
	}
	
	public Class<?> getRootMenuClass();
	public String[] getChildMenuPackages();
	
	public Class<?> getIPermissionClass();
	
//	public IPermission createPermission(PermissionType type);
	
	public Class<?> getIMenuClass();
	
	public Class<?> getIFunctionClass();
	
}
