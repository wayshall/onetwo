package org.onetwo.ext.security.utils;

import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;

public class SecuritySessionUserManager implements SessionUserManager<UserDetail> {

	@Override
	public UserDetail getCurrentUser() {
		return SecurityUtils.getCurrentLoginUser();
	}
	
	

}
