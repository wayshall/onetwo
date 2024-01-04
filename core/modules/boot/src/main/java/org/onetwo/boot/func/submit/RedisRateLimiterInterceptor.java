package org.onetwo.boot.func.submit;

import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptor;
import org.onetwo.boot.func.submit.RedisRateLimiter.ActionContext;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

public class RedisRateLimiterInterceptor implements MvcInterceptor {

	@Autowired
	private RedisRateLimiter redisRateLimiter;
	
	private String limiterKey;
	private int periodInSeconds = 5;
	private int timesInPeriod = 1;
	private String errMsg = "操作太频繁，请休息一下";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		String key = limiterKey;
		if (StringUtils.isBlank(key)) {
			key = RequestUtils.getUrlPathHelper().getRequestUri(request);
		}
		
		ActionContext actionContext = new ActionContext();
		actionContext.setActionKey(key);
		actionContext.setTimesInPeriod(timesInPeriod);
		actionContext.setPeriod(periodInSeconds);
		actionContext.setErrorMessage(errMsg);
		actionContext.setPeriodUnit(TimeUnit.SECONDS);
		redisRateLimiter.consumeAction(actionContext);
		
		return true;
	}

}
