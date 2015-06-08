package org.onetwo.common.web.login;

import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.utils.UserDetail;

@SuppressWarnings("rawtypes")
public class EmptyUserLoginService extends AbstractUserLoginServiceImpl {

	@Override
	protected ValidatableUser findUniqueUser(LoginParams loginParams) {
		throw new UnsupportedOperationException("findUniqueUser");
	}

	@Override
	protected UserDetail createLoginUser(String token, ValidatableUser user) {
		throw new UnsupportedOperationException("createLoginUser");
	}
	
	

}
