package org.onetwo.common.sso;

import java.util.Map;

import org.onetwo.common.utils.UserDetail;

@SuppressWarnings("rawtypes")
public interface UserDataFetcher {
	 
	IUserEntity loadUserEntity(String userName, Map params);
	IUserLoginEntity insertLoginLog(IUserEntity user, Map params);
	UserDetail buildUserInfo(IUserEntity userEntity, IUserLoginEntity userLogin);
	
	IUserLoginEntity findUserLogin(String token);
	void saveUserLogin(IUserLoginEntity userLogin);

}
