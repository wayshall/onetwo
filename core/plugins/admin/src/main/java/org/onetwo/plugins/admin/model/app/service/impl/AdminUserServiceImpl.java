package org.onetwo.plugins.admin.model.app.service.impl;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminUserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

@Transactional
@Service
public class AdminUserServiceImpl extends HibernateCrudServiceImpl<AdminUserEntity, Long> {

	@Resource
	private AdminAppServiceImpl adminAppServiceImpl;
	
	public AdminUserServiceImpl(){
	}

	public AdminUserEntity saveUserRoles(long userId, long[] roleIds){
		AdminUserEntity user = load(userId);
		if(roleIds==null || roleIds.length==0)
		{
			user.setRoles(null);
			save(user);
			return user;
		}
		
		List<AdminRoleEntity> rolelist = this.getBaseEntityManager().findByProperties(AdminRoleEntity.class, "id:in", roleIds);
		Set<AdminRoleEntity> assignPermSet = new HashSet<AdminRoleEntity>(rolelist);
		Set<AdminRoleEntity> adds = Sets.difference(assignPermSet, new HashSet<AdminRoleEntity>(user.getRoles()));
		Set<AdminRoleEntity> deletes = Sets.difference(new HashSet<AdminRoleEntity>(user.getRoles()), assignPermSet);
		user.getRoles().removeAll(deletes);
		user.getRoles().addAll(adds);
		save(user);
		return user;
	}
	public AdminUserEntity findUserWithRoles(Long id){
		AdminUserEntity user = findById(id);
		Hibernate.initialize(user.getRoles());
		return user;
	}
	public void removeUsers(long[] ids){
		for(long id : ids){
			AdminUserEntity user = this.load(id);
//			user.getRoles().clear();
			remove(user);
		}
	}
}