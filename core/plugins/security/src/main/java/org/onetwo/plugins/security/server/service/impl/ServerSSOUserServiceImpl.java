package org.onetwo.plugins.security.server.service.impl;

import javax.annotation.Resource;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.sso.SSOUserService;
import org.onetwo.plugins.security.server.SsoServerConfig;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.slf4j.Logger;
import org.springframework.remoting.RemoteAccessException;

public class ServerSSOUserServiceImpl implements SSOUserService {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private SessionStorer sessionStorer;
	
	@Resource
	private SsoServerConfig ssoServerConfig;

	@Override
	public UserDetail getCurrentLoginUserByToken(String token, String sign) {
		boolean valid = SecurityPluginUtils.checkSign(token, ssoServerConfig.getSignKey(), sign);
		if(!valid){
			logger.error("check sign error, token[{}], sign[{}]", token, sign);
			throw new RemoteAccessException("invalid token");
		}
		UserDetail user = sessionStorer.getUser(token);
		return user;
	}
	

}
