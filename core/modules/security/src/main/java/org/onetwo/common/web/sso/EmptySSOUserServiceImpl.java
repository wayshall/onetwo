package org.onetwo.common.web.sso;

import org.onetwo.common.utils.UserDetail;

public class EmptySSOUserServiceImpl implements SSOUserService {

	@Override
	public UserDetail getCurrentLoginUserByToken(String token) {
		return null;
	}

}
