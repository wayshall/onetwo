package org.onetwo.boot.module.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * @author weishao zeng
 * <br/>
 */
public class RootUserInterceptor extends MvcInterceptorAdapter {

	@Autowired
	private SessionUserManager<GenericUserDetail<?>> sessionUserManager;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		GenericUserDetail<?> userDetail = sessionUserManager.getCurrentUser();
		if (logger.isInfoEnabled()) {
			logger.info("sessionUserManager: {}, userDetail: {}", sessionUserManager, userDetail);
		}
		if (UserRoot.class.isInstance(userDetail) && ((UserRoot)userDetail).isSystemRootUser()) {
			return true;
		} else {
			throw new NoAuthorizationException();
		}
	}
}

