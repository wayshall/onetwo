package org.onetwo.webapp.manager.utils;

import org.onetwo.plugins.admin.AdminModule;




public interface Systems {
	String name = "后台管理系统";
	String appCode = Systems.class.getSimpleName();
	
	Class<?>[] children = new Class<?>[]{AdminModule.class};
	
	/*@ProxyMenu(value=AdminModule.class)
	public interface AdminMgr {
	}*/

}
