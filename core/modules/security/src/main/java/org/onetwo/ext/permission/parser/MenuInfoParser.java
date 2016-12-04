package org.onetwo.ext.permission.parser;

import java.util.Map;

import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.ext.permission.entity.DefaultIPermission;

public interface MenuInfoParser<P extends DefaultIPermission<P>> {
	
	public PermissionConfig<P> getMenuInfoable();
	
	public abstract P parseTree();
	public abstract String getCode(Class<?> permClass);
	
	public String getRootMenuCode();
	public Map<String, P> getPermissionMap();
	public P getPermission(Class<?> clazz);
	public P getRootMenu();

}