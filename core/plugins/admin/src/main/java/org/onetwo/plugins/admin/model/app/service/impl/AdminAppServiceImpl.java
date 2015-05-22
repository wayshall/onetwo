package org.onetwo.plugins.admin.model.app.service.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.fish.exception.JFishServiceException;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.plugins.admin.model.app.entity.AdminAppEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminMenuEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminPermissionEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminUserEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity.RoleStatus;
import org.onetwo.plugins.admin.model.app.entity.AdminUserEntity.UserStatus;
import org.onetwo.plugins.admin.model.vo.ZTreeMenuModel;
import org.onetwo.plugins.admin.utils.WebConstant;
import org.onetwo.plugins.permission.entity.IPermission;
import org.onetwo.plugins.permission.service.PluginPermissionManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

@Transactional
@Service
public class AdminAppServiceImpl extends HibernateCrudServiceImpl<AdminAppEntity, String> {

	@Resource
	private PluginPermissionManager permissionManager;
	
	@Cacheable(value=WebConstant.METHOD_CACHE_ETERNAL)
	public List<AdminAppEntity> loadAllApps(){
		return this.findAll();
	}
	
	public AdminUserEntity loadUser(Long userId){
		AdminUserEntity appUser = getBaseEntityManager().load(AdminUserEntity.class, userId);
		return appUser;
	}
	

	public AdminUserEntity findAppUser(Long userId){
		//enabled dataqueryfilter
		AdminUserEntity appUser = getBaseEntityManager().findUnique(AdminUserEntity.class, "id", userId);
		return appUser;
	}
	public AdminRoleEntity findAppRole(Long roleId){
		//enabled dataqueryfilter
		AdminRoleEntity appRole = getBaseEntityManager().findUnique(AdminRoleEntity.class, "id", roleId);
		return appRole;
	}
	

	public AdminUserEntity saveAppUser(AdminUserEntity user){
		int count = getBaseEntityManager().countRecord(AdminUserEntity.class, "userName", user.getUserName(), "id:!=", user.getId(), "status:!=", UserStatus.DELETE, K.IF_NULL, IfNull.Ignore).intValue();
		if(count>0)
			throw new ServiceException("该用户名已存在：" + user.getUserName());
		
		if(StringUtils.isNotBlank(user.getConfirmPassword())){
			user.setPassword(MDFactory.getMDEncrypt("SHA").encrypt(user.getConfirmPassword()));
		}else{
			user.setPassword(null);
		}

//		return this.getBaseEntityManager().save(user);
		if(user.getId()!=null){
			AdminUserEntity dbuser = loadUser(user.getId());
			HibernateUtils.copyWithoutRelations(user, dbuser);
			getBaseEntityManager().save(dbuser);
			return dbuser;
		}else{
			if(user.getStatus()==null){
				user.setStatus(UserStatus.NORMAL);
			}
			return this.getBaseEntityManager().save(user);
		}
	}

	/***
	 * 保存角色
	 * @param roleVo
	 * @return
	 */
	public AdminRoleEntity saveAppRole(AdminRoleEntity roleVo) {
		AdminRoleEntity dbrole = null;
		String areaCode = null;
		if(roleVo.getId()!=null){//如果是更新
			dbrole = getBaseEntityManager().load(AdminRoleEntity.class, roleVo.getId());
			if(dbrole.isSystemRoot()){
				throw JFishServiceException.createByMsg("系统管理员不允许修改！");
			}
			HibernateUtils.copy(roleVo, dbrole);
		}else{
			dbrole = roleVo;
			if(dbrole.getStatus()==null)
				dbrole.setStatus(RoleStatus.NORMAL);
//			dbrole.addApp(appServiceImpl.load(WebConstant.APP_CODE));
//			dbrole.setAppCode(WebConstant.APP_CODE);
		}
		/*if(dbrole.getArea()!=null)
			areaCode = dbrole.getArea().getAreaCode();*/
		int count = this.getBaseEntityManager().countRecord(AdminRoleEntity.class, "name", roleVo.getName(), "id:!=", roleVo.getId(), "status", RoleStatus.NORMAL, K.IF_NULL, IfNull.Ignore).intValue();
		if(count>0)
			throw new ServiceException("角色名称已存在：" + roleVo.getName());
//			throw JFishServiceException.create(AdminErrorCodes.UNIQUE_DATA, "角色名称["+roleVo.getName()+"]");
		
		getBaseEntityManager().save(dbrole);
		return dbrole;
	}
	
	/***
	 * 分配角色
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	public AdminUserEntity saveUserRoles(long userId, long[] roleIds){
		BaseEntityManager bm = getBaseEntityManager();
		AdminUserEntity user = bm.load(AdminUserEntity.class, userId);
		if(user.isSystemRoot())
			throw new ServiceException("root用户无需修改角色！");
		if(LangUtils.isEmpty(roleIds)){
			user.setRoles(null);
			bm.save(user);
			return user;
		}
		
		List<AdminRoleEntity> rolelist = this.getBaseEntityManager().findByProperties(AdminRoleEntity.class, "id:in", roleIds);
		Set<AdminRoleEntity> assignPermSet = new HashSet<AdminRoleEntity>(rolelist);
		
		Set<AdminRoleEntity> adds = Sets.difference(assignPermSet, new HashSet<AdminRoleEntity>(user.getRoles()));
		Set<AdminRoleEntity> deletes = Sets.difference(new HashSet<AdminRoleEntity>(user.getRoles()), assignPermSet);
		user.getRoles().removeAll(deletes);
		user.getRoles().addAll(adds);
		bm.save(user);
		
		return user;
	}
	
	public List<AdminRoleEntity> findAppRoles(String appCode){
		Assert.hasText(appCode);
		return getBaseEntityManager().findByProperties(AdminRoleEntity.class, "appCode", appCode, "status",RoleStatus.NORMAL);
	}
	
	/***
	 * 获取指定角色在app下的权限
	 * @param appCode
	 * @param role
	 * @return
	 */
	public List<AdminPermissionEntity> findRoleAppPermissions(String appCode, AdminRoleEntity role){
		Session session = this.getBaseEntityManager().getRawManagerObject(SessionFactory.class).getCurrentSession();
		List<AdminPermissionEntity> perms = session.createFilter(role.getPermissions(), "where appCode = ?").setParameter(0, appCode).list();
		return perms;
	}
	public List<AdminPermissionEntity> findRoleAppPermissions(String appCode, long roleId){
		AdminRoleEntity role = findAppRole(roleId);
		return findRoleAppPermissions(appCode, role);
	}

	/***
	 * 根据角色查找菜单
	 * @param appCode
	 * @param ids
	 * @return
	 */
	public Set<AdminMenuEntity> findRolesMenus(String appCode, List<Long> ids){
		if(LangUtils.isEmpty(ids))
			return Collections.EMPTY_SET;
		
		Set<AdminMenuEntity> menus = new HashSet<AdminMenuEntity>();
		Set<AdminPermissionEntity> permissions = findRolesPermissions(appCode, ids);
		for(IPermission perm : permissions){
			if(AdminMenuEntity.class.isInstance(perm))
				menus.add((AdminMenuEntity)perm);
		}
		
		return menus;
	}
	

	/****
	 * 根据角色查找权限
	 * @param ids
	 * @return
	 */
	public Set<AdminPermissionEntity> findRolesPermissions(String appCode, List<Long> ids){
		if(LangUtils.isEmpty(ids))
			return Collections.EMPTY_SET;
		
		List<AdminRoleEntity> roles = getBaseEntityManager().findByProperties(AdminRoleEntity.class, "id:in", ids);
		return findRolesPermissions(appCode, roles.toArray(new AdminRoleEntity[0]));
	}
	
	public Set<AdminPermissionEntity> findRolesPermissions(String appCode, AdminRoleEntity... roles){
		if(LangUtils.isEmpty(roles))
			return Collections.EMPTY_SET;
		
		Set<AdminPermissionEntity> permissions = new HashSet<AdminPermissionEntity>();
		for(AdminRoleEntity role : roles){
			List<AdminPermissionEntity> perms = findRoleAppPermissions(appCode, role);
			permissions.addAll(perms);
		}
		
		return permissions;
	}
	
	@Transactional(readOnly=true)
	public List<AdminMenuEntity> findAppMenus(String appCode){
		List<AdminMenuEntity> menulist = (List<AdminMenuEntity>)permissionManager.findAppMenus(AdminMenuEntity.class, appCode);
		return menulist;
	}
	

	@Transactional(readOnly=true)
	public List<ZTreeMenuModel> findAllAsTree(String appCode, List<String> checkedPermissions){
		return findAllAsTree(appCode, null, checkedPermissions);
	}
	
	@Transactional(readOnly=true)
	public List<ZTreeMenuModel> findAllAsTree(String appCode, List<Long> roleIds, List<String> checkedPermissions){
		List<AdminPermissionEntity> permlist = null;
		if(LangUtils.isEmpty(roleIds)){
			permlist= findAppPermissions(appCode);
		}else{
			Set<AdminPermissionEntity> permset = this.findRolesPermissions(appCode, roleIds);//roleServiceImpl.findRolesPermissions(roleIds);
			if(LangUtils.isEmpty(permset))
				throw new ServiceException("你没有该系统的任何权限，无法分配给其他角色！");
			permlist = new ArrayList<AdminPermissionEntity>(permset);
		}
		
		List<ZTreeMenuModel> trees = ZTreeMenuModel.asZtree(permlist, checkedPermissions);
		return trees;
	}
	
	@Transactional(readOnly=true)
	public List<AdminPermissionEntity> findAppPermissions(String appCode) {
		return (List<AdminPermissionEntity>)permissionManager.findAppPermissions(AdminPermissionEntity.class, appCode);
	}

	/****
	 * 保存权限
	 * @param roleId
	 * @param permissionCodes
	 */
	public void saveRolePermissions(String appCode, long roleId, String[] permissionCodes){
		AdminRoleEntity role = getBaseEntityManager().load(AdminRoleEntity.class, roleId);
		if(permissionCodes==null || permissionCodes.length==0){
			role.getPermissions().clear();
			return ;
		}
//		role.getPermissions().clear();
		List<AdminPermissionEntity> permlist = (List<AdminPermissionEntity>)permissionManager.findPermissionByCodes(AdminPermissionEntity.class, appCode, permissionCodes);
		Set<AdminPermissionEntity> assignPermSet = new HashSet<AdminPermissionEntity>(permlist);
		

		List<AdminPermissionEntity> appPerms = findRoleAppPermissions(appCode, role);

//		Set<PermissionEntity> rolePerms = new HashSet<PermissionEntity>(role.getPermissions());
		Set<AdminPermissionEntity> adds = Sets.difference(assignPermSet, new HashSet<AdminPermissionEntity>(appPerms));
		Set<AdminPermissionEntity> deletes = Sets.difference(new HashSet<AdminPermissionEntity>(appPerms), assignPermSet);
		role.getPermissions().removeAll(deletes);
		role.getPermissions().addAll(adds);
		getBaseEntityManager().save(role);
	}
}