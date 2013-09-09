package org.onetwo.plugins.permission.entity;

public enum PermissionType {
	MENU("菜单"),
	FUNCTION("功能");
//	DATAFIELD
	
	private final String name;

	private PermissionType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String getValue() {
		return toString();
	}
		
}
