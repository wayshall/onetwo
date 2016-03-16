package org.onetwo.boot.plugins.permission;

import java.lang.reflect.Field;

import org.onetwo.boot.plugins.permission.entity.AbstractPermission;
import org.onetwo.common.reflect.ReflectUtils;



abstract public class AbstractPermissionConfig<P extends AbstractPermission<P>> implements org.onetwo.boot.plugins.permission.PermissionConfig<P> {

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
