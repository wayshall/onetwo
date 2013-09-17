package org.onetwo.plugins.permission;

import java.util.Map;

import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;

public interface MenuInfoParser {

	public PermissionConfig getMenuInfoable();
	
	public abstract IMenu<?, ?> parseTree();
	public abstract String parseCode(Class<?> permClass);
	
	public String getRootMenuCode();
	public Map<String, ? extends IPermission> getMenuNodeMap();
	public IPermission getMenuNode(Class<?> clazz);
	public IMenu<?, ?> getRootMenu();

}