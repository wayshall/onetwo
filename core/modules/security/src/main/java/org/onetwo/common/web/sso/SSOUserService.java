package org.onetwo.common.web.sso;

import org.onetwo.common.utils.UserDetail;

public interface SSOUserService {

	public UserDetail getCurrentLoginUserByToken(String token, String sign);
	
}
