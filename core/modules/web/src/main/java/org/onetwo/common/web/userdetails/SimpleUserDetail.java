package org.onetwo.common.web.userdetails;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SimpleUserDetail implements UserDetail, UserRoot, Serializable {
	
	private long userId;
	private String userName;
	private String nickName;
//	private String token;

	public SimpleUserDetail(){ 
	}
	
	public SimpleUserDetail(long userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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
		return UserRoot.ROOT_USER_ID==getUserId();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
