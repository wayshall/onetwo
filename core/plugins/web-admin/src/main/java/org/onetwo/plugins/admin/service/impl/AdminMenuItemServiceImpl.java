package org.onetwo.plugins.admin.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.impl.DefaultMenuItemRepository;
import org.onetwo.ext.permission.utils.PermissionUtils;
import org.onetwo.plugins.admin.dao.AdminPermissionDao;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminMenuItemServiceImpl extends DefaultMenuItemRepository {

	@Autowired
	private AdminPermissionDao adminPermissionDao;
	
	public AdminMenuItemServiceImpl(){
	}
	
	public List<PermisstionTreeModel> findUserMenus(UserDetail loginUser) {
		/*List<Permission> permissions = findUserAppPermissions(null, loginUser);
		return createMenuTreeBuilder(permissions).buidTree();*/
		//修改为可不选择父节点后，修改构建菜单树的方法
		List<AdminPermission> allDatas = this.adminPermissionDao.findPermissions(null);
		Map<String, AdminPermission> allPermissions = allDatas.stream()
								.filter(p->PermissionUtils.isMenu(p))
								.collect(Collectors.toMap(AdminPermission::getCode, p->p));
		
		List<AdminPermission> permissions = findUserAppPermissions(null, loginUser);
		TreeBuilder<PermisstionTreeModel> tb = createMenuTreeBuilder(permissions);
		tb.buidTree(node->{
			AdminPermission p = allPermissions.get(node.getParentId());
			return convertToMenuTreeModel(p);
		});
		return tb.getRootNodes();
	}

	
	private PermisstionTreeModel convertToMenuTreeModel(AdminPermission p){
		PermisstionTreeModel pm = new PermisstionTreeModel(p.getCode(), p.getName(), p.getParentCode());
		pm.setSort(p.getSort());
		pm.setUrl(p.getUrl());
		return pm;
	}

	public List<AdminPermission> findUserAppPermissions(String appCode, UserDetail userDetail) {
		List<AdminPermission> adminPermissions = this.adminPermissionDao.findAppPermissionsByUserId(appCode, userDetail.getUserId());
		List<AdminPermission> permList = adminPermissions.stream()
				.filter(p->PermissionUtils.isMenu(p))
				.collect(Collectors.toList());
		return permList;
	}
}
