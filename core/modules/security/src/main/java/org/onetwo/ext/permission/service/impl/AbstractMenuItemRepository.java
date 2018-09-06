package org.onetwo.ext.permission.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.tree.TreeModel;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.PermissionConfigAdapter;
import org.onetwo.ext.permission.PermissionManager;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.service.MenuItemRepository;

abstract public class AbstractMenuItemRepository<T extends TreeModel<T>, P extends IPermission> implements MenuItemRepository<T> {
	
	@Resource
	private PermissionManager<P> permissionManager;

	@Resource
	private PermissionConfigAdapter<P> permissionConfig;
	
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
