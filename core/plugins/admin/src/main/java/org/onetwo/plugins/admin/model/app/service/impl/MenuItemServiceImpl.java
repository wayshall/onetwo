package org.onetwo.plugins.admin.model.app.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.PermissionDetail;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.plugins.admin.model.app.entity.AdminMenuEntity;
import org.onetwo.plugins.admin.model.app.service.MenuItemRegistry;
import org.onetwo.plugins.admin.model.vo.ExtMenuModel;
import org.onetwo.plugins.permission.PermissionConfig;
import org.springframework.transaction.annotation.Transactional;

//@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemRegistry{

	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Resource
	private PermissionConfig permissionConfig;
	
	@Override
	public Collection<ExtMenuModel> findAllMenus() {
		List<AdminMenuEntity> menulist = baseEntityManager.findByProperties(AdminMenuEntity.class, "code:like", permissionConfig.getRootMenuClass().getSimpleName()+"%");
		List<ExtMenuModel> extMenus = LangUtils.newArrayList(menulist.size());
		for(AdminMenuEntity menu :menulist){
			extMenus.add(convert(menu));
		}
		return extMenus;
		
	}
	
	private ExtMenuModel convert(AdminMenuEntity menu){
		ExtMenuModel extMenu = new ExtMenuModel(menu.getCode(), menu.getName(), menu.getUrl());
		extMenu.setSort(menu.getSort());
		if(menu.getParent()!=null){
			extMenu.setParentId(menu.getParent().getCode());
		}
		return extMenu;
	}

	@Override
	public Collection<ExtMenuModel> findUserMenus(UserDetail userDetail) {
		/*System.out.println("..findUserMenus..");
		
		List<UserEntity> users=this.baseEntityManager.findByProperties(UserEntity.class, "userName",loginUser.getUserName());
		UserEntity user=null;
		if(users.size()>0){
			user=users.get(0);
		}else{
			 throw new ServiceException("用户不存在！");
		}*/
		if(userDetail.isSystemRootUser()){
			return findAllMenus();
		}else{
			return findMenus((PermissionDetail) userDetail);
		}
	}
	
	public Collection<ExtMenuModel>findMenus(PermissionDetail loginUser) {
		if(loginUser.getPermissions().isEmpty())
			throw new NoAuthorizationException("没有分配任何权限，请联系管理员！");
		List<AdminMenuEntity> menulist =baseEntityManager.findByProperties(AdminMenuEntity.class, "code:in",loginUser.getPermissions());
		List<ExtMenuModel> extMenus = LangUtils.newArrayList(menulist.size());
		for(AdminMenuEntity menu :menulist){
			extMenus.add(convert(menu));
		}
		return extMenus;
	}

}
