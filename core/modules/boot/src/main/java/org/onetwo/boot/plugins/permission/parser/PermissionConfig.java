package org.onetwo.boot.plugins.permission.parser;

import java.lang.reflect.Field;

import org.onetwo.boot.plugins.permission.entity.IPermission;
import org.onetwo.common.utils.ReflectUtils;



public interface PermissionConfig<P extends IPermission<P>> {

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
	
	public Class<P> getIPermissionClass();
	
}
