package org.onetwo.ext.permission;

import java.lang.reflect.Field;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.permission.entity.DefaultIPermission;



abstract public class AbstractPermissionConfig<P extends DefaultIPermission<P>> implements org.onetwo.boot.plugins.permission.PermissionConfig<P> {

	@Override
	public String getAppCode(){
		String appCode = "";
		Field appCodeField = ReflectUtils.findField(getRootMenuClass(), "appCode");
		if(appCodeField!=null){
			appCode = (String)ReflectUtils.getFieldValue(appCodeField, getRootMenuClass(), false);
		}else{
			appCode = getRootMenuClass().getSimpleName();
		}
		return appCode;
	}
	
}
