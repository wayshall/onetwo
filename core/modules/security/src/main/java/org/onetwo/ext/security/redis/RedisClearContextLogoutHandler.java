package org.onetwo.ext.security.redis;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.ext.security.log.LoginLogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class RedisClearContextLogoutHandler extends SimpleUrlLogoutSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisSecurityContextRepository redisSecurityContextRepository;

	@Autowired(required=false)
	private LoginLogHandler loginLogHandler;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		try {
			redisSecurityContextRepository.removeSecurityContext(request, response);
		} catch (Exception e) {
			logger.error("removeSecurityContext error: " + e.getMessage(), e);
		}
		super.onLogoutSuccess(request, response, authentication);
		
		try {
			if(loginLogHandler!=null){
				loginLogHandler.logoutSuccess(request, response, authentication);
			}
		} catch (Exception e) {
			logger.error("logoutSuccess error: " + e.getMessage(), e);
		}
	}

	public void setLoginLogHandler(LoginLogHandler loginLogHandler) {
		this.loginLogHandler = loginLogHandler;
	}
	
}
