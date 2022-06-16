package org.onetwo.common.web.userdetails;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SimpleUserDetail implements UserDetail, UserRoot, Serializable {
	
	private Long userId;
	private String userName;
	private String nickName;
//  @ApiModelProperty("头像")
	private String avatar;
//	private String token;
	private UserTypes userType;

	public SimpleUserDetail(){ 
	}
	
	public SimpleUserDetail(Long userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.nickName = userName;
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
	public boolean isSystemRootUser() {
		return getUserId().equals(ROOT_USER_ID);
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

	public UserTypes getUserType() {
		return userType;
	}

	public void setUserType(UserTypes userType) {
		this.userType = userType;
	}

}
