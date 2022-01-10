package org.onetwo.common.web.userdetails;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SimpleUserDetail implements GenericUserDetail<Serializable>, UserRoot, Serializable {
	
	private Serializable userId;
	private String userName;
	private String nickName;
//  @ApiModelProperty("头像")
	private String avatar;
//	private String token;

	public SimpleUserDetail(){ 
	}
	
	public SimpleUserDetail(Serializable userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.nickName = userName;
	}

	public Serializable getUserId() {
		return userId;
	}

	public void setUserId(Serializable userId) {
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

}
