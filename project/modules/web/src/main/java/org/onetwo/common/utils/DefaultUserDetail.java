package org.onetwo.common.utils;


import java.util.Date;
import java.util.List;


@SuppressWarnings("serial")
public class DefaultUserDetail implements UserDetail, RoleDetail, PermissionDetail, UserActivityCheckable {
	
	protected Date lastActivityTime = new Date();
	private long userId;
//	private Long organId;
	private String userName;
	private String token;

	protected Date lastSynchronizedTime = new Date(); 

	public DefaultUserDetail(){ 
		this.lastActivityTime = new Date();
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

	/*@Override
	public Long getDepartmentId() {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public List<String> getPermissions() {
		return null;
	}

	public void setPermissions(List<String> permissions) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String[] getRoles() {
		return null;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder("{");
		sb.append("userName:").append(userName)
		.append(",token:").append(token)
		.append("}");
		return sb.toString();
	}
	
	
}
