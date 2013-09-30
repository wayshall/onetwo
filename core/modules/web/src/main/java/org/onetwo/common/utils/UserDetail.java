package org.onetwo.common.utils;


/****
 * 用户会话信息接口
 * @author weishao
 */
public interface UserDetail extends SsoTokenable {  
	
	public final static String USER_DETAIL_KEY = "loginUserInfo"; 
	

	public String getUserName();
	
	public long getUserId();

	/***
	 * 是否root管理员
	 * @return
	 */
	public boolean isSystemRootUser();
	
//	public Long getOrganId();
	
//	public Long getDepartmentId();
/*
	public void  setPermissions(List<String> permissions);

	public List<String> getPermissions();*/
	/*
	public void setLastActivityTime(Date lastActivityTime);
	public Date getLastActivityTime();

	public Date getLastSynchronizedTime();
	public void setLastSynchronizedTime(Date lastSynchronizedTime);*/
	
}
