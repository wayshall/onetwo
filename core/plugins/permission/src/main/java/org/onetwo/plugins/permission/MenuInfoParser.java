package org.onetwo.plugins.permission;

import java.util.Map;

import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.entity.PermissionEntity;

public interface MenuInfoParser {

	public abstract MenuEntity parseTree();
	public abstract String parseCode(Class<?> permClass);
	
	public String getRootMenuCode();
	public Map<String, PermissionEntity> getMenuNodeMap();
	public PermissionEntity getMenuNode(Class<?> clazz);
	public MenuEntity getRootMenu();

}