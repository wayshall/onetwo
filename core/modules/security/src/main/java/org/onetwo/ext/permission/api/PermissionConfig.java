package org.onetwo.ext.permission.api;




public interface PermissionConfig<P extends IPermission<P>> {

	public String getAppCode();
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
	
	public Class<?> getRootMenuClass();
	default public String[] getChildMenuPackages(){
		return null;
	}
	
	public Class<P> getIPermissionClass();
	
	/*default public int menuCacheInitSize(){
		return 300;
	}*/
	
}
