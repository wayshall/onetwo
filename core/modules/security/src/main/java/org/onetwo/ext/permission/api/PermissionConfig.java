package org.onetwo.ext.permission.api;

import java.lang.reflect.Field;

import org.onetwo.common.reflect.ReflectUtils;




public interface PermissionConfig<P extends IPermission> {

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
	/*
	default public String getAppCode(){
		String appCode = "";
		Field appCodeField = ReflectUtils.findField(getRootMenuClass(), "appCode");
		if(appCodeField!=null){
			appCode = (String)ReflectUtils.getFieldValue(appCodeField, getRootMenuClass(), false);
		}else{
			appCode = getRootMenuClass().getSimpleName();
		}
		return appCode;
	}*/
	/****
	 * 菜单配置类
	 * @return
	 */
	public Class<?> getRootMenuClass();
	default public String[] getChildMenuPackages(){
		return null;
	}
	/*****
	 * 权限实体类
	 * @return
	 */
	public Class<P> getIPermissionClass();
	
	/*default public int menuCacheInitSize(){
		return 300;
	}*/
	
}
