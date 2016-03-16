package org.onetwo.boot.plugins.permission;

import java.util.stream.Stream;

public enum PermissionType {
	MENU("菜单"),
	FUNCTION("功能"),
	RESOURCE("资源");
//	DATAFIELD
	
	private final String label;

	private PermissionType(String name) {
		this.label = name;
	}

	public String getLabel() {
		return label;
	}
	
	public String getValue() {
		return name();
	}
	
	public static PermissionType of(String code){
		return Stream.of(values()).filter(pt->pt.name().equals(code))
									.findFirst()
									.orElse(RESOURCE);
	}
		
}
