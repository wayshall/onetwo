package org.onetwo.common.web.userdetails;

/****
 * 用户会话信息接口
 * @author weishao
 */
public interface UserDetail extends UserRoot {  
	
	public final static String USER_DETAIL_KEY = "loginUserInfo"; 
//	final Long SYSTEM_ROOT_USER_ID = 1L;
	

	public String getUserName();
	
	public long getUserId();
	
	/*default boolean isAnonymousLogin() {
		return false;
	}*/
	
}
