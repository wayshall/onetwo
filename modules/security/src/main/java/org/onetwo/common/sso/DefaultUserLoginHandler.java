package org.onetwo.common.sso;

import java.util.Date;
import java.util.Map;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.SessionTimeoutException;
import org.onetwo.common.exception.SystemErrorCode.LoginErrorCode;
import org.onetwo.common.utils.UserActivityCheckable;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.encrypt.MDFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DefaultUserLoginHandler implements UserLoginHandler{
	
	protected UserDataFetcher userDataFetcher;

	public DefaultUserLoginHandler(UserDataFetcher userDataFetcher) {
		this.userDataFetcher = userDataFetcher;
	}

	/********
	 * login
	 */
	public UserDetail login(String userName, String password, Map params) {
		UserDetail loginUser = null;
		
		IUserEntity user = loadUserEntity(userName, params);
		this.validatePassword(user, password);
		this.validateUserState(user);
		
		IUserLoginEntity userLogin = userDataFetcher.insertLoginLog(user, params);
		
		loginUser = userDataFetcher.buildUserInfo(user, userLogin);
		
		return loginUser;
	}

	protected IUserEntity loadUserEntity(String userName, Map params) {
		IUserEntity user = userDataFetcher.loadUserEntity(userName, params);
		if(user==null)
			throw new LoginException("用户和密码不匹配["+userName+"].", LoginErrorCode.USER_NOT_FOUND);
		return user;
	}

	protected void validatePassword(IUserEntity user, String password) {
//		if (!user.getUserPassword().equals(password))
		if (!MDFactory.checkEncrypt(password, user.getUserPassword()))
			throw new LoginException("用户和密码不匹配["+user.getUserAccount()+"].", LoginErrorCode.PASSWORD_ERROR);
	}

	protected void validateUserState(IUserEntity user) {
		if(!user.isUserAvailable()){
			throw new LoginException("用户状态不正常。", LoginErrorCode.USER_STATE_ERROR);
		}
	}
	
	/*********
	 * login by token
	 */
	public UserDetail loginByToken(String token){
		UserDetail userDetail = null;
		
		IUserLoginEntity<Long> userLogin = findUserLogin(token);
		this.validateUserLogin(userLogin);
		
		IUserEntity user = loadUserEntity(userLogin.getLoginName(), null);
		
		userDetail = this.userDataFetcher.buildUserInfo(user, userLogin);
		userDetail.setToken(userLogin.getUserLoginToken());
		
		return userDetail;
	}
	
	protected void validateUserLogin(IUserLoginEntity<Long> userLogin){
		if(!userLogin.isUserLoging())
			throw new SessionTimeoutException();
	}

	protected IUserLoginEntity findUserLogin(String token){
		IUserLoginEntity userLogin = userDataFetcher.findUserLogin(token);
		if(userLogin==null)
			throw new LoginException("没有找到此令牌的用户，请先登陆", LoginErrorCode.NO_TOKEN);
		return userLogin;
	}
	
	
	/******
	 * 登出
	 * @param userDetail
	 * @param normalLogout
	 */
	public void logout(UserDetail userDetail, boolean normalLogout){
		IUserLoginEntity userLogin = this.findUserLogin(userDetail.getToken());
		if(normalLogout)
			userLogin.userLogout();
		else
			userLogin.unNormalUserLogout();
		
		if(UserActivityCheckable.class.isInstance(userDetail)){
			UserActivityCheckable checkable = (UserActivityCheckable)userDetail;
			if(checkable.getLastActivityTime()!=null)
				userLogin.setLastlogTime(checkable.getLastActivityTime());
		}
		userLogin.setLogoutTime(new Date());
		userDataFetcher.saveUserLogin(userLogin);
	}
}