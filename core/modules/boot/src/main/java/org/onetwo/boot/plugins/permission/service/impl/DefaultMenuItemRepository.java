package org.onetwo.boot.plugins.permission.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.onetwo.boot.plugins.permission.PermissionManager;
import org.onetwo.boot.plugins.permission.entity.IPermission;
import org.onetwo.boot.plugins.permission.entity.PermisstionTreeModel;
import org.onetwo.boot.plugins.permission.parser.PermissionConfig;
import org.onetwo.boot.plugins.permission.service.MenuItemRepository;
import org.onetwo.common.utils.TreeBuilder;

public class DefaultMenuItemRepository implements MenuItemRepository<PermisstionTreeModel> {
	
	@Resource
	private PermissionManager<? extends IPermission<?>> permissionManager;

	@Resource
	private PermissionConfig<? extends IPermission<?>> permissionConfig;
	
	@Override
    public Collection<PermisstionTreeModel> findAllMenus() {
		List<? extends IPermission<?>> permissions = permissionManager.findAppMenus(permissionConfig.getAppCode());
		List<PermisstionTreeModel> pmlist = permissions.stream().map(p->{
			PermisstionTreeModel pm = new PermisstionTreeModel(p.getCode(), p.getName(), p.getParentCode());
			pm.setUrl(p.getUrl());
			return pm;
		}).collect(Collectors.toList());
		
		TreeBuilder<PermisstionTreeModel> builder = new TreeBuilder<>(pmlist);
		List<PermisstionTreeModel> rootTree = builder.buidTree();
	    return rootTree.get(0).getChildren();
    }


}
