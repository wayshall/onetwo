package org.onetwo.common.web.sso;

import org.onetwo.common.sso.CurrentLoginUserParams;
import org.onetwo.common.utils.UserDetail;

public interface SSOUserService {

	public UserDetail getCurrentLoginUser(CurrentLoginUserParams params);
	
}
