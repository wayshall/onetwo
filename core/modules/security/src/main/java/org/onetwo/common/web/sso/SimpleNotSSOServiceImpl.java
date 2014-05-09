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
	
	public UserDetail checkLogin(SecurityTarget target) {
		UserDetail authoritable = target.getAuthoritable();
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
