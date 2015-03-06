package org.onetwo.common.web.sso;

import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.sso.UserLoginService;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.springframework.core.Ordered;

/****
 * 默认的非sso登录的SSOService实现
 * @author wayshall
 *
 */
public class SimpleNotSSOServiceImpl extends AbstractSSOServiceImpl implements UserLoginService, Ordered  {
	
	/*public static final String LOGIN_KEYSTORE = "login.keystore";//session, cookie
	private static enum KeyStore{
		SESSION,
		COOKIE
	}
*/
	@Override
	public UserDetail login(LoginParams loginParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void logout(UserDetail userDetail, boolean normal) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public UserActivityTimeHandler getUserActivityTimeHandler() {
		throw new UnsupportedOperationException();
	}
	
	public boolean checkTimeout(SecurityTarget target, boolean updateLastLogTime){
		return false;
	}
	
	/*public KeyStore getKeyStore(){
		String keystr = BaseSiteConfig.getInstance().getProperty(LOGIN_KEYSTORE, KeyStore.SESSION.toString());
		KeyStore keyStore = KeyStore.valueOf(keystr.toUpperCase());
		return keyStore;
	}*/
	
	public UserDetail checkLogin(SecurityTarget target) {
		UserDetail authoritable = target.getAuthoritable();
		/*KeyStore ks = getKeyStore();
		switch (ks) {
			case SESSION :
				authoritable = target.getAuthoritable();
				break;
				
			case COOKIE :
				authoritable = super.checkLogin(target);
				break;
	
			default:
				throw new ServiceException("not supported key store");
		}*/
		return authoritable;
	}
/*	@Override
	protected UserDetail createUserDetail(SecurityTarget target) {
		DefaultUserDetail user = new DefaultUserDetail();
		user.setToken(target.getCookieToken());
		return user;
	}*/

	@Override
	protected UserDetail getCurrentLoginUserByCookieToken(String token) {
		return null;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}


}
