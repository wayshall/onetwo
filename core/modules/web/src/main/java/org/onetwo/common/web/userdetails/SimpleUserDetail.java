package org.onetwo.common.web.userdetails;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class SimpleUserDetail implements UserDetail, UserRoot, Serializable {
	public static final List<String> ADMIN_ROLES = Lists.newArrayList("ENT_ADMIN", "ADMIN");
	
	private Long userId;
	private String userName;
	private String nickName;
//  @ApiModelProperty("头像")
	private String avatar;
//	private String token;
	private UserTypes userType;
	private Boolean systemRootUser;
	private Boolean adminRole;
	private String roles;

	public SimpleUserDetail(){ 
	}
	
	public SimpleUserDetail(Long userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.nickName = userName;
		this.systemRootUser = userId.equals(ROOT_USER_ID);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public Boolean isSystemRootUser() {
		return systemRootUser;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserTypes getMallUserType() {
		return userType;
	}

	public void setUserType(UserTypes userType) {
		this.userType = userType;
	}

	public Boolean getAdminRole() {
		return adminRole;
	}

	public void setAdminRole(Boolean adminRole) {
		this.adminRole = adminRole;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

}
