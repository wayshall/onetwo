package org.onetwo.common.sso;

import java.util.Map;

import org.onetwo.common.utils.UserDetail;

public interface UserLoginService {
	
	public UserDetail login(String username, String password, Map<?, ?> params);
	
	public void logout(UserDetail userDetail, boolean normal);
	
	/*public void setLoginInfoCache(UserDetail userDetail);
	public void clearLoginInfoCache();*/

}