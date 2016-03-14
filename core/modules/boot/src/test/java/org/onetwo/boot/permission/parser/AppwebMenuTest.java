package org.onetwo.boot.permission.parser;



public interface AppwebMenuTest {
	String name = "管理系统";
	String appCode = AppwebMenuTest.class.getSimpleName();

	public static interface System {
		String name = "系统管理";
		int sort = 1;
		Class<?>[] children = new Class<?>[]{AdminModule.class};
	}
}
