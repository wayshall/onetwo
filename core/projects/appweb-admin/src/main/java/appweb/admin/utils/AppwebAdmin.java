package appweb.admin.utils;

import org.onetwo.plugins.admin.AdminModule;
import org.onetwo.plugins.admin.DataModule;


public interface AppwebAdmin {
	String name = "管理系统";
	String appCode = AppwebAdmin.class.getSimpleName();

	public static interface System {
		String name = "系统管理";
		int sort = 1;
		Class<?>[] children = new Class<?>[]{AdminModule.class, DataModule.class};
	}
}
