package org.example.model.member.vo;

import java.util.Collection;

import org.example.model.member.entity.RoleEntity;
import org.onetwo.common.utils.DefaultUserDetail;

@SuppressWarnings("serial")
public class LoginUserInfo extends DefaultUserDetail {

	private Collection<RoleEntity> userRoles;

	public Collection<RoleEntity> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Collection<RoleEntity> userRoles) {
		this.userRoles = userRoles;
	}



}
