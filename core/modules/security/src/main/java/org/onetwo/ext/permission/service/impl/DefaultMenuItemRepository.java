package org.onetwo.ext.permission.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.userdetails.UserRoot;
import org.onetwo.ext.permission.PermissionManager;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.onetwo.ext.permission.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@Transactional
public class DefaultMenuItemRepository implements MenuItemRepository<PermisstionTreeModel> {
	
	@Autowired
	protected PermissionManager permissionManager;

	/*@Autowired
	private PermissionConfigAdapter<? extends DefaultIPermission<?>> permissionConfig;*/
	
	@SuppressWarnings("unchecked")
	@Override
    public List<PermisstionTreeModel> findAllMenus() {
//		List<? extends DefaultIPermission<?>> permissions = permissionManager.findAppMenus(permissionConfig.getAppCode());
		List<IPermission> permissions = permissionManager.findAppMenus(null);
	    return createMenuTreeBuilder(permissions).buidTree();
//	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
    }
	
	protected TreeBuilder<PermisstionTreeModel> createMenuTreeBuilder(List<? extends IPermission> permissions){
	    return PermissionUtils.createMenuTreeBuilder(permissions);
	}

	@Override
	public List<PermisstionTreeModel> findUserMenus(UserDetail loginUser) {
//		List<? extends DefaultIPermission<?>> permissions = permissionManager.findUserAppMenus(permissionConfig.getAppCode(), loginUser);
//		List<IPermission> permissions = permissionManager.findUserAppMenus(null, loginUser);
//	    return createMenuTreeBuilder(permissions).buidTree();
	    return findUserMenus(loginUser, (userPerms, allPerms)->{
	    	TreeBuilder<PermisstionTreeModel> tb = createMenuTreeBuilder(userPerms);
	    	List<PermisstionTreeModel> roots = tb.buidTree();
	    	return roots;
	    });
//	    return createMenuTreeBuilder(permissions).buidTree().get(0).getChildren();
	}
	
//	@Override
	@SuppressWarnings("unchecked")
	public <E> List<E> findUserMenus(UserDetail loginUser, TreeMenuBuilder<E> builder) {
		if(loginUser==null){
			throw new NotLoginException();
		}
		
		List<? extends IPermission> permissions = null;
		if(UserRoot.class.isInstance(loginUser) && ((UserRoot)loginUser).isSystemRootUser()){
			permissions = permissionManager.findAppMenus(null);
		}else{
			// 这个是查询菜单的接口，所以只查菜单数据，若需要包含非菜单的权限数据，应该另外调用permissionManager.findUserAppPerms查询，避免混乱
//			permissions = permissionManager.findUserAppMenus(null, loginUser);
			permissions = permissionManager.findUserAppPerms(null, loginUser);
		}
	    return builder.build(permissions, getAllPermissions());
	}
	
	protected Map<String, ? extends IPermission> getAllPermissions(){
		return Collections.emptyMap();
	}

	@Override
	public <E> List<E> findUserPermissions(UserDetail loginUser, TreeMenuBuilder<E> builder) {
		throw new NotImplementedException("findUserPermissions");
	}


}
