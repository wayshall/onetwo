package org.onetwo.boot.plugins.permission.parser;

import java.util.Map;

import org.onetwo.boot.plugins.permission.AbstractPermissionConfig;
import org.onetwo.boot.plugins.permission.entity.DefaultIPermission;

public interface MenuInfoParser<P extends DefaultIPermission<P>> {
	
	public AbstractPermissionConfig<P> getMenuInfoable();
	
	public abstract P parseTree();
	public abstract String getCode(Class<?> permClass);
	
	public String getRootMenuCode();
	public Map<String, P> getPermissionMap();
	public P getPermission(Class<?> clazz);
	public P getRootMenu();

}