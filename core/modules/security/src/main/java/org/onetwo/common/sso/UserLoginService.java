package org.onetwo.common.sso;

import org.onetwo.common.utils.UserDetail;

public interface UserLoginService {
	
	public UserDetail login(LoginParams loginParams);
	
	public void logout(UserDetail userDetail, boolean normal);
	
	/*public void setLoginInfoCache(UserDetail userDetail);
	public void clearLoginInfoCache();*/

}