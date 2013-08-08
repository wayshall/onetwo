package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.List;


public class SimpleUserDetail implements UserDetail, RoleDetail {
	
	private long userId;
	private String userName;
	private String token;
	private List<String> roles = new ArrayList<String>();

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
	
	@Override
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public void addRole(String role) {
		this.roles.add(role);
	}
	
	public boolean isRole(String role){
		return roles.contains(role);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder("{");
		sb.append("userName:").append(userName)
		.append(",token:").append(token)
		.append("}");
		return sb.toString();
	}
	
	
}
