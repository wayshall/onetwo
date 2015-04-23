package org.onetwo.plugins.admin.model.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.SystemErrorCode.LoginErrorCode;
import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.utils.DefaultUserDetail;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.login.AbstractUserLoginServiceImpl;
import org.onetwo.common.web.login.ValidatableUser;
import org.onetwo.plugins.admin.model.app.entity.AdminPermissionEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminRoleEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminUserEntity;
import org.onetwo.plugins.admin.model.app.entity.AdminUserEntity.UserStatus;
import org.onetwo.plugins.permission.PermissionConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultNotSSOServiceImpl extends AbstractUserLoginServiceImpl<AdminUserEntity>{

	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Resource
	private AdminAppServiceImpl adminAppServiceImpl;
	
	@Resource
	private PermissionConfig permissionConfig;
	
	@Override
	protected ValidatableUser<AdminUserEntity> findUniqueUser(LoginParams loginParams) {
		final AdminUserEntity userData = baseEntityManager.findUnique(AdminUserEntity.class, "userName", loginParams.getUserName(), "status:!=", UserStatus.DELETE);;
		if(userData==null)
			throw new LoginException("没有此用户或密码错误！", LoginErrorCode.USER_NOT_FOUND);
		return new ValidatableUser<AdminUserEntity>() {

			@Override
			public AdminUserEntity getUserData() {
				return userData;
			}

			@Override
			public String getUserName() {
				return userData.getUserName();
			}

			@Override
			public String getPassword() {
				return userData.getPassword();
			}

			@Override
			public boolean isInvalidUserStatus() {
				return userData.getStatus()!=UserStatus.NORMAL;
			}
			
		};
	}

	@Override
	protected UserDetail createLoginUser(String token, ValidatableUser<AdminUserEntity> validableUser) {
		DefaultUserDetail loginUser = new DefaultUserDetail(validableUser.getUserData().getId(), validableUser.getUserName(), token);
		AdminUserEntity user = baseEntityManager.load(AdminUserEntity.class, loginUser.getUserId());
		loginUser.setNickName(user.getNickName());

		//role and permission
		List<AdminPermissionEntity> permlist = null;
		for(AdminRoleEntity role : user.getRoles()){
			if(role.isSystemRoot()){
				permlist = adminAppServiceImpl.findAppPermissions(permissionConfig.getAppCode());//this.baseEntityManager.findByProperties(PermissionEntity.class, "code:like", Menu.class.getSimpleName()+"%", "hidden", false);
//				List<String> perms = permlist.stream().map(e-> e.getCode()).collect(Collectors.toList());
//				loginUser.setPermissions(perms);
				break;
			}else{
				if(permlist==null){
					permlist = new ArrayList<>();
				}
				List<AdminPermissionEntity> perms = adminAppServiceImpl.findRoleAppPermissions(permissionConfig.getAppCode(), role);
				permlist.addAll(perms);
			}
		}
		List<String> perms = permlist.stream().map(e-> e.getCode()).collect(Collectors.toList());
		loginUser.setPermissions(perms);
		
		return loginUser;
	}

}
