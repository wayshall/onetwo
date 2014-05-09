package org.onetwo.plugins.security.server.service.impl;

import javax.annotation.Resource;

import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.sso.SSOUserService;

public class ServerSSOUserServiceImpl implements SSOUserService {

	@Resource
	private SessionStorer sessionStorer;

	@Override
	public UserDetail getCurrentLoginUserByToken(String token) {
		UserDetail user = sessionStorer.getUser(token);
		return user;
	}
	

}
