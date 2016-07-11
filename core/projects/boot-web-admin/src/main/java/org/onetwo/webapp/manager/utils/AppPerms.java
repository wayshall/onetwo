package org.onetwo.webapp.manager.utils;

import org.onetwo.plugins.admin.AdminModule;




public interface AppPerms {
	String name = "后台管理系统";
	String appCode = AppPerms.class.getSimpleName();
	
	Class<?>[] children = new Class<?>[]{AdminModule.class};
	
	/*@ProxyMenu(value=AdminModule.class)
	public interface AdminMgr {
	}*/

}
