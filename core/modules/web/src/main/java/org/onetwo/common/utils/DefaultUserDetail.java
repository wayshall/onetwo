package org.onetwo.common.utils;


import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class DefaultUserDetail implements UserDetail, RoleDetail, RoleIdDetail, PermissionDetail, Serializable {
	
	protected Date lastActivityTime = new Date();
	private long userId;
//	private Long organId;
	private String userName;
	private String nickName;
	private String token;
	private List<String> permissions = LangUtils.newArrayList();
	private List<String> roles = LangUtils.newArrayList();

	protected Date lastSynchronizedTime = new Date(); 

	public DefaultUserDetail(){ 
		this.lastActivityTime = new Date();
	}
	
	public DefaultUserDetail(long userId, String userName, String token) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.token = token;
	}

	public Date getLastActivityTime() {
		return lastActivityTime;
	}

	public void setLastActivityTime(Date lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastSynchronizedTime() {
		return lastSynchronizedTime;
	}

	public void setLastSynchronizedTime(Date lastSynchronizedTime) {
		this.lastSynchronizedTime = lastSynchronizedTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	/*public Long getOrganId() {
		return organId;
	}
	
	public void setOrganId(Long organId) {
		this.organId = organId;
	}*/
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	@Override
	public List<String> getRoles() {
		return roles;
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
		return getRoleIds()!=null && getRoleIds().contains(SYSTEM_ROOT_ROLE_ID);
	}

	@Override
	public List<Long> getRoleIds() {
		return Collections.EMPTY_LIST;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
}
