package org.onetwo.boot.plugins.permission.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.boot.plugins.permission.AbstractPermissionConfig;
import org.onetwo.boot.plugins.permission.PermissionManager;
import org.onetwo.boot.plugins.permission.entity.DefaultIPermission;
import org.onetwo.boot.plugins.permission.service.MenuItemRepository;
import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.tree.TreeModel;
import org.onetwo.common.web.userdetails.UserDetail;

abstract public class AbstractMenuItemRepository<T extends TreeModel<T>, P extends DefaultIPermission<P>> implements MenuItemRepository<T> {
	
	@Resource
	private PermissionManager<P> permissionManager;

	@Resource
	private AbstractPermissionConfig<P> permissionConfig;
	
	@Override
    public List<T> findAllMenus() {
		List<P> permissions = permissionManager.findAppMenus(permissionConfig.getAppCode());
	    return createMenuTreeBuilder(permissions).buidTree();
//	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
    }
	
	abstract protected TreeBuilder<T> createMenuTreeBuilder(List<P> permissions);

	@Override
	public List<T> findUserMenus(UserDetail loginUser) {
		List<P> permissions = permissionManager.findUserAppMenus(permissionConfig.getAppCode(), loginUser);
	    return createMenuTreeBuilder(permissions).buidTree();
//	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
	}


}
