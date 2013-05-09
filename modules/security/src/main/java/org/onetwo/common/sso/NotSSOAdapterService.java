package org.onetwo.common.sso;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;

abstract public class NotSSOAdapterService implements SSOService {

	@Override
	public boolean checkTimeout(SecurityTarget target, boolean updateLastLogTime) {
		return false;
	}

	@Override
	public UserDetail checkLogin(SecurityTarget target) {
		return target.getAuthoritable();
	}

	@Override
	public void logout(UserDetail userDetail, boolean normal) {
	}


}
