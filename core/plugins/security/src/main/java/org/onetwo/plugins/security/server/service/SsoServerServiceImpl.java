package org.onetwo.plugins.security.server.service;

import javax.annotation.Resource;

import org.onetwo.common.sso.UserActivityTimeHandler;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.common.web.sso.AbstractSSOServiceImpl;
import org.onetwo.plugins.security.server.SessionStorer;

public class SsoServerServiceImpl extends AbstractSSOServiceImpl {

	@Resource
	private SessionStorer sessionStorer;
	
	public boolean checkTimeout(SecurityTarget target, boolean updateLastLogTime){
		return false;
	}
	@Override
	public UserActivityTimeHandler getUserActivityTimeHandler() {
		return null;
	}

	@Override
	protected UserDetail getCurrentLoginUserByCookieToken(String token) {
		return sessionStorer.getUser(token);
	}

}
