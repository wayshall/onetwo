package org.onetwo.groovy.test;

import org.onetwo.plugins.admin.AdminModule;
import org.onetwo.plugins.admin.DataModule;

public interface MenuTest {

	String name = "管理系统";
	String appCode = MenuTest.class.getSimpleName();

	public static interface System {
		String name = "系统管理";
		int sort = 1;
		Class<?>[] children = new Class<?>[]{AdminModule.class, DataModule.class};
	}
}
