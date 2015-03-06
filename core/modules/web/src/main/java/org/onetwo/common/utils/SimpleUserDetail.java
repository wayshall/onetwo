package org.onetwo.common.utils;

import java.io.Serializable;


@SuppressWarnings("serial")
public class SimpleUserDetail implements UserDetail, Serializable {
	
	private long userId;
	private String userName;
	private String token;

	public SimpleUserDetail(){ 
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String toString(){
		StringBuilder sb = new StringBuilder("{");
		sb.append("userName:").append(userName)
		.append(",token:").append(token)
		.append("}");
		return sb.toString();
	}

	@Override
	public boolean isSystemRootUser() {
		return false;
	}
	
	
}
