package org.onetwo.plugins.admin.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.userdetails.UserRoot;
import org.onetwo.ext.permission.api.IPermission;
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

	/***
	 * 查找用户权限（含权限和菜单）
	 * @author wayshall
	 * @param loginUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PermisstionTreeModel> findUserPermissions(UserDetail loginUser, TreeMenuBuilder<PermisstionTreeModel> builder) {
		if(loginUser==null){
			throw new NotLoginException();
		}
		
		List<? extends IPermission> permissions = null;
		if(UserRoot.class.isInstance(loginUser) && ((UserRoot)loginUser).isSystemRootUser()){
			permissions = permissionManager.findAppPermissions(null);
		}else{
			permissions = this.adminPermissionDao.findAppPermissionsByUserId(null, loginUser.getUserId());
		}
		
		List<AdminPermission> allDatas = this.adminPermissionDao.findPermissions(null);
		Map<String, AdminPermission> allPermissions = allDatas.stream()
//								.filter(p->PermissionUtils.isMenu(p))
								.collect(Collectors.toMap(AdminPermission::getCode, p->p));
		return builder.build(permissions, allPermissions);
	}

	/****
	 * 查找用户菜单
	 * @author wayshall
	 * @param loginUser
	 * @return
	 */
	public List<PermisstionTreeModel> findUserMenus(UserDetail loginUser) {
		if(loginUser==null){
			throw new NotLoginException();
		}else if(UserRoot.class.isInstance(loginUser) && ((UserRoot)loginUser).isSystemRootUser()){
			return findAllMenus();
		}
		
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

	
	public static PermisstionTreeModel convertToMenuTreeModel(AdminPermission p){
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
