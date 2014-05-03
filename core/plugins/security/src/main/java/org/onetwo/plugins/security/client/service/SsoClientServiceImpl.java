package org.onetwo.plugins.security.client.service;

import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.sso.AbstractSSOServiceImpl;

public class SsoClientServiceImpl extends AbstractSSOServiceImpl {


	@Override
	public UserActivityTimeHandler getUserActivityTimeHandler() {
		return null;
	}


	@Override
	protected UserDetail getCurrentLoginUserByCookieToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
