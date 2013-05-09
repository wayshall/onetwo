package org.onetwo.common.sso;


@SuppressWarnings("unchecked")
public interface UserActivityTimeDataFetcher {

	IUserLoginEntity findUserLogin(String token);
	
	void saveUserLogin(IUserLoginEntity userLogin);
}
