package org.onetwo.plugins.admin.model.data.service.impl

import javax.annotation.Resource

import org.onetwo.common.db.BaseEntityManager
import org.onetwo.common.exception.ServiceException
import org.onetwo.common.utils.encrypt.MDFactory
import org.onetwo.plugins.admin.model.app.entity.AdminAppEntity
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity
import org.onetwo.plugins.admin.model.app.entity.AdminUserEntity
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity.RoleCode
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity.RoleStatus
import org.onetwo.plugins.admin.model.app.entity.AdminUserEntity.UserStatus
import org.onetwo.plugins.permission.PermissionConfig
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DataInitServiceImpl {

	@Resource
	private BaseEntityManager baseEntityManager;
	
//	@Resource
//	private MenuInfoParser menuInfoParser;
	@Resource
	private PermissionConfig menuInfoable;
	
	public void initDbdata(){
		AdminAppEntity app = new AdminAppEntity()
		app.code = menuInfoable.getAppCode()
		app.name = menuInfoable.getRootMenuClass().name;
		
		if(baseEntityManager.countRecord(AdminAppEntity.class, "code", app.code).intValue()>0){
			throw new ServiceException("不能重复初始化");
		}
		baseEntityManager.save(app)
		
		AdminRoleEntity role = new AdminRoleEntity()
		role.appCode = app.code
		role.name = "系统管理员"
		role.remark = "拥有所有权限的角色，一般用作维护。"
		role.code = "COMMON"
		role.status = RoleStatus.NORMAL
		role.roleCode = RoleCode.ROOT.name()
		baseEntityManager.save(role)
		
		AdminUserEntity user = new AdminUserEntity()
		user.appCode = app.code
		user.gender = 0
		user.nickName = "系统管理员"
		user.password = MDFactory.getMDEncrypt("SHA").encrypt("123456")
		user.status = UserStatus.NORMAL
		user.userName = "root"
		baseEntityManager.save(user);
		
		user.addRole(role);
	}
}
