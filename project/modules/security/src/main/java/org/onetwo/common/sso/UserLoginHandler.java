package org.onetwo.common.sso;

import java.util.Map;

import org.onetwo.common.utils.UserDetail;

@SuppressWarnings("unchecked")
public interface UserLoginHandler {

	public UserDetail login(String userName, String password, Map params);
	public UserDetail loginByToken(String token);
	
	public void logout(UserDetail userDetail, boolean normalLogout);
}