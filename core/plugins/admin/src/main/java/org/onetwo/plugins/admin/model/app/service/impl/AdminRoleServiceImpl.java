package org.onetwo.plugins.admin.model.app.service.impl;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.fish.exception.JFishServiceException;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.admin.model.app.entity.AdminPermissionEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity.RoleStatus;
import org.onetwo.plugins.admin.utils.AdminErrorCodes;
import org.onetwo.plugins.permission.entity.IPermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminRoleServiceImpl extends HibernateCrudServiceImpl<AdminRoleEntity, Long> {
	
	
	@Resource
	private AdminAppServiceImpl adminAppServiceImpl;

	public Set<AdminPermissionEntity> findRolePermission(long id){
		AdminRoleEntity role = findById(id);
		Hibernate.initialize(role.getPermissions());
		return role.getPermissions();
	}

	/****
	 * 根据角色查找权限
	 * @param ids
	 * @return
	 */
	public Set<AdminPermissionEntity> findRolesPermissions(String appCode, List<Long> ids){
		if(LangUtils.isEmpty(ids))
			return Collections.EMPTY_SET;
		
		Set<AdminPermissionEntity> permissions = new HashSet<AdminPermissionEntity>();
		List<AdminRoleEntity> roles = this.findByProperties("id:in", ids);
		for(AdminRoleEntity role : roles){
			for(IPermission perm : adminAppServiceImpl.findRoleAppPermissions(appCode, role)){
				permissions.add((AdminPermissionEntity)perm);
			}
		}
		
		return permissions;
	}
	
	
	public void removeRoles(long[] ids){
		if(ids==null || ids.length==0)
			return ;
		for(Long id : ids){
			AdminRoleEntity role = load(id);
			remove(role);
		}
	}
	public AdminRoleEntity saveBaseInfo(String appCode, AdminRoleEntity roleVo) {
		AdminRoleEntity dbrole = null;
//		String areaCode = null;
		if(roleVo.getId()!=null){//如果是更新
			dbrole = load(roleVo.getId());
			if(dbrole.isSystemRoot()){
				throw JFishServiceException.createByMsg("系统管理员不允许修改！");
			}
			HibernateUtils.copy(roleVo, dbrole);
		}else{
			dbrole = roleVo;
			if(dbrole.getStatus()==null)
				dbrole.setStatus(RoleStatus.NORMAL);
//			dbrole.addApp(appServiceImpl.load(WebConstant.APP_CODE));
			dbrole.setAppCode(appCode);
		}
		int count = this.countRecord("name", roleVo.getName(), "id:!=", roleVo.getId(), "status", RoleStatus.NORMAL, K.IF_NULL, IfNull.Ignore).intValue();
		if(count>0)
			throw JFishServiceException.create(AdminErrorCodes.UNIQUE_DATA, "本地区角色名称["+roleVo.getName()+"]");
		
		save(dbrole);
		return dbrole;
	}
}