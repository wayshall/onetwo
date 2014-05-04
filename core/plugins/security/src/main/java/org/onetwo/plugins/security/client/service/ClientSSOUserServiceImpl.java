package org.onetwo.plugins.security.client.service;

import javax.annotation.Resource;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.sso.SSOUserService;

public class ClientSSOUserServiceImpl implements SSOUserService {

	@Resource
	private SSOUserService ssoUserServiceProxy;

	@Override
	public UserDetail getCurrentLoginUserByToken(String token) {
		UserDetail user = ssoUserServiceProxy.getCurrentLoginUserByToken(token);
		return user;
	}

}
