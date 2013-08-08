package org.onetwo.common.web.sso;

import java.util.Date;
import java.util.Map;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.SessionTimeoutException;
import org.onetwo.common.exception.SystemErrorCode.LoginErrorCode;
import org.onetwo.common.sso.IUserEntity;
import org.onetwo.common.sso.IUserLoginEntity;
import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.sso.UserDataFetcher;
import org.onetwo.common.utils.SsoTokenable;
import org.onetwo.common.utils.UserActivityCheckable;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.encrypt.MDFactory;

/***********
 * 不建议使用
 * @author wayshall
 *
 */
abstract public class AbstractUserSSOService extends AbstractSSOServiceImpl {

	abstract public UserDataFetcher getUserDataFetcher();

	@SuppressWarnings("rawtypes")
	@Override
	public UserDetail login(String userCode, String password, Map params) {
		UserDetail loginUser = null;
		
		IUserEntity<?> user = getUserDataFetcher().loadUserEntity(userCode, params);
		this.validatePassword(user, password);
		this.validateUserState(user);
		
		IUserLoginEntity<?> userLogin = getUserDataFetcher().insertLoginLog(user, params);
		
		loginUser = getUserDataFetcher().buildUserInfo(user, userLogin);
		
		return loginUser;
	}

	/*protected IUserEntity<?> _loadUserEntity(String userName, Map<?, ?> params) {
		IUserEntity<?> user = getUserDataFetcher().loadUserEntity(userName, params);
		if(user==null)
			throw new LoginException("用户和密码不匹配["+userName+"].", LoginErrorCode.USER_NOT_FOUND);
		return user;
	}*/

	protected void validatePassword(IUserEntity<?> user, String password) {
//		if (!user.getUserPassword().equals(password))
		if (!MDFactory.checkEncrypt(password, user.getUserPassword()))
			throw new LoginException("用户和密码不匹配["+user.getUserAccount()+"].", LoginErrorCode.PASSWORD_ERROR);
	}

	protected void validateUserState(IUserEntity<?> user) {
		if(!user.isUserAvailable()){
			throw new LoginException("用户状态不正常。", LoginErrorCode.USER_STATE_ERROR);
		}
	}

	/******
	 * 登出
	 * @param userDetail
	 * @param normalLogout
	 */
	public void logout(UserDetail userDetail, boolean normalLogout){
		IUserLoginEntity<?> userLogin = this.loadUserLoginByToken(userDetail.getToken());
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
		getUserDataFetcher().saveUserLogin(userLogin);
	}
	

	/*********
	 * login by token
	 */
	protected UserDetail getCurrentLoginUserByCookieToken(String token){
		UserDetail userDetail = null;
		
		IUserLoginEntity<?> userLogin = loadUserLoginByToken(token);
		this.validateUserLogin(userLogin);
		
		IUserEntity<?> user = getUserDataFetcher().loadUserEntity(userLogin.getLoginName(), null);
		/*if(user==null){
			throw new LoginException("找不到此用户", LoginErrorCode.USER_NOT_FOUND);
		}*/
		
		userDetail = this.getUserDataFetcher().buildUserInfo(user, userLogin);
		userDetail.setToken(userLogin.getUserLoginToken());
		
		return userDetail;
	}
	
	protected void validateUserLogin(IUserLoginEntity<?> userLogin){
		if(!userLogin.isUserLoging())
			throw new SessionTimeoutException();
	}

	protected IUserLoginEntity<?> loadUserLoginByToken(String token){
		IUserLoginEntity<?> userLogin = getUserDataFetcher().findUserLogin(token);
		if(userLogin==null)
			throw new LoginException("没有找到此令牌的用户，请先登陆", LoginErrorCode.NO_TOKEN);
		return userLogin;
	}

	@Override
	public UserActivityTimeHandler getUserActivityTimeHandler() {
		throw new UnsupportedOperationException("没有实现此操作");
	}

}
