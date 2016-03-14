package org.onetwo.common.web.userdetails;


/****
 * 用户会话信息接口
 * @author weishao
 */
public interface UserDetail {  
	
	public final static String USER_DETAIL_KEY = "loginUserInfo"; 
//	final Long SYSTEM_ROOT_USER_ID = 1L;
	

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
