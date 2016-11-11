package org.onetwo.ext.permission.service.impl;

import java.util.List;

import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.PermissionConfigAdapter;
import org.onetwo.ext.permission.PermissionManager;
import org.onetwo.ext.permission.entity.DefaultIPermission;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.onetwo.ext.permission.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultMenuItemRepository implements MenuItemRepository<PermisstionTreeModel> {
	
	@Autowired
	private PermissionManager<? extends DefaultIPermission<?>> permissionManager;

	@Autowired
	private PermissionConfigAdapter<? extends DefaultIPermission<?>> permissionConfig;
	
	@Override
    public List<PermisstionTreeModel> findAllMenus() {
		List<? extends DefaultIPermission<?>> permissions = permissionManager.findAppMenus(permissionConfig.getAppCode());
	    return createMenuTreeBuilder(permissions).buidTree();
//	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
    }
	
	protected TreeBuilder<PermisstionTreeModel> createMenuTreeBuilder(List<? extends DefaultIPermission<?>> permissions){
	    return PermissionUtils.createMenuTreeBuilder(permissions);
	}

	@Override
	public List<PermisstionTreeModel> findUserMenus(UserDetail loginUser) {
		List<? extends DefaultIPermission<?>> permissions = permissionManager.findUserAppMenus(permissionConfig.getAppCode(), loginUser);
	    return createMenuTreeBuilder(permissions).buidTree();
//	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
	}


}
