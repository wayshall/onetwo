package org.onetwo.common.sso;

import java.util.Date;

@SuppressWarnings("rawtypes")
public class DefaultUserActivityTimeHandler implements UserActivityTimeHandler{
	
	private UserActivityTimeDataFetcher fetcher;
	
	public DefaultUserActivityTimeHandler(UserActivityTimeDataFetcher fetcher){
		this.fetcher = fetcher;
	}
	
	/*****
	 * update time
	 * @param token
	 * @return
	 */
	public void updateUserLastActivityTime(String token, Date time) {
		IUserLoginEntity userLogin = findUserLogin(token);
		if(userLogin==null)
			return ;
		userLogin.setLastlogTime(time);
		saveUserLogin(userLogin);
	}
	public SSOLastActivityStatus getUserLastActivityStatus(String token) {
		IUserLoginEntity ul = this.findUserLogin(token);
		if(ul==null)
			return null;
		SSOLastActivityInfo lastLoagInfo = new SSOLastActivityInfo();
		lastLoagInfo.setLogin(ul.isUserLoging());
		lastLoagInfo.setLastActivityTime(ul.getLastlogTime());
		return lastLoagInfo;
	}
	

	protected IUserLoginEntity findUserLogin(String token){
		return fetcher.findUserLogin(token);
	}
	
	protected void saveUserLogin(IUserLoginEntity userLogin){
		fetcher.saveUserLogin(userLogin);
	}
	
}
