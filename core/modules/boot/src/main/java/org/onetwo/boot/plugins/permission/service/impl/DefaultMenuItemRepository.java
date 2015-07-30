package org.onetwo.boot.plugins.permission.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.boot.plugins.permission.PermissionManager;
import org.onetwo.boot.plugins.permission.entity.IPermission;
import org.onetwo.boot.plugins.permission.entity.PermisstionTreeModel;
import org.onetwo.boot.plugins.permission.parser.PermissionConfig;
import org.onetwo.boot.plugins.permission.service.MenuItemRepository;
import org.onetwo.boot.plugins.permission.utils.PermissionUtils;
import org.onetwo.common.utils.TreeBuilder;
import org.onetwo.common.web.userdetails.UserDetail;

public class DefaultMenuItemRepository implements MenuItemRepository<PermisstionTreeModel> {
	
	@Resource
	private PermissionManager<? extends IPermission<?>> permissionManager;

	@Resource
	private PermissionConfig<? extends IPermission<?>> permissionConfig;
	
	@Override
    public Collection<PermisstionTreeModel> findAllMenus() {
		List<? extends IPermission<?>> permissions = permissionManager.findAppMenus(permissionConfig.getAppCode());
	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
    }
	
	protected TreeBuilder<PermisstionTreeModel> createMenuTreeBuilder(List<? extends IPermission<?>> permissions){
	    return PermissionUtils.createMenuTreeBuilder(permissions);
	}

	@Override
	public Collection<PermisstionTreeModel> findUserMenus(UserDetail loginUser) {
		List<? extends IPermission<?>> permissions = permissionManager.findUserAppMenus(permissionConfig.getAppCode(), loginUser);
	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
	}


}
