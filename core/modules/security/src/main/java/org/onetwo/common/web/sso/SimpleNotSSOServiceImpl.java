package org.onetwo.common.web.sso;

import java.util.Map;

import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.sso.UserLoginService;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;

public class SimpleNotSSOServiceImpl extends AbstractSSOServiceImpl implements UserLoginService  {

	private EmptySSOUserServiceImpl emptySSOUserServiceImpl = new EmptySSOUserServiceImpl();
	
	@Override
	public UserDetail login(String username, String password, Map params) {
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
	public SSOUserService getSSOUserService() {
		return emptySSOUserServiceImpl;
	}


}
