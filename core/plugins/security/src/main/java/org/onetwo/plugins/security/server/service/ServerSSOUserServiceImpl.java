package org.onetwo.plugins.security.server.service;

import javax.annotation.Resource;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.sso.SSOUserService;
import org.onetwo.plugins.security.server.SessionStorer;

public class ServerSSOUserServiceImpl implements SSOUserService {

	@Resource
	private SessionStorer sessionStorer;

	@Override
	public UserDetail getCurrentLoginUserByToken(String token) {
		UserDetail user = sessionStorer.getUser(token);
		return user;
	}
	

}
