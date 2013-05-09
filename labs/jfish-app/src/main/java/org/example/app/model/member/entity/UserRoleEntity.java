package org.example.app.model.member.entity;

import java.io.Serializable;

import org.onetwo.common.fish.annotation.JFishEntity;
import org.onetwo.common.fish.orm.MappedType;

@JFishEntity(table = "T_USER_ROLE", type=MappedType.JOINED)
public class UserRoleEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3071554018677252113L;
	private Long userId;
	private Long roleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
