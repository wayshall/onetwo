package org.onetwo.common.web.userdetails;

import java.io.Serializable;

/****
 * 用户会话信息接口
 * @author weishao
 */
public interface GenericUserDetail<ID extends Serializable> extends UserRoot {  
	
	final static String USER_DETAIL_KEY = "loginUserInfo"; 

	String getUserName();
	
	ID getUserId();
	
	default UserTypes getUserType() {
		return UserTypes.USER;
	}
	
}
