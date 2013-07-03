package org.onetwo.common.utils;

import java.io.Serializable;

/****
 * 用户会话信息接口
 * @author weishao
 */
public interface UserDetail extends Serializable {  
	
	public final static String USER_DETAIL_KEY = "loginUserInfo"; 
	
	public final static String TOKEN_KEY = "token"; 

	public String getUserName();
	
	public long getUserId();
	
//	public Long getOrganId();
	
//	public Long getDepartmentId();
	
	public String getToken();
	
	public void setToken(String token);
/*
	public void  setPermissions(List<String> permissions);

	public List<String> getPermissions();*/
	/*
	public void setLastActivityTime(Date lastActivityTime);
	public Date getLastActivityTime();

	public Date getLastSynchronizedTime();
	public void setLastSynchronizedTime(Date lastSynchronizedTime);*/
	
}
