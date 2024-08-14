package org.onetwo.ext.security.utils;

import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.SessionUserManager;

public class SecuritySessionUserManager implements SessionUserManager<GenericUserDetail<?>> {

	@Override
	public GenericUserDetail<?> getCurrentUser() {
		return SecurityUtils.getCurrentLoginUser();
	}

}
