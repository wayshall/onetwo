package org.onetwo.boot.func.submit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptor;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * 自动使用当前请求的url和用户id生成key，并检查是否频繁提交
 * @author weishao zeng
 * <br/>
 */
public class UserSubmitCheckerInterceptor implements MvcInterceptor {
	
	@Autowired
	private RedisRateLimiter redisRateLimiter;
	@Autowired
	private SessionUserManager<? extends GenericUserDetail<?>> sessionManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		GenericUserDetail<?> loginUser = sessionManager.getCurrentUser();
		if (loginUser==null) {
			throw new NotLoginException();
		}
		String requestUri = RequestUtils.getUrlPathHelper().getRequestUri(request);
		String key = loginUser.getUserId().toString() + ":" + requestUri;
		redisRateLimiter.checkAction(key);
		return true;
	}

}

