package org.onetwo.boot.module.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.module.session.SessionProperties.SessionStrategies;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.Assert;

import lombok.Setter;

/**
 * @author weishao zeng
 * <br/>
 */
public class CustomizableHttpSessionStrategy implements HttpSessionStrategy, InitializingBean {
	@Setter
	private String strategyHeaderName;
	@Setter
	private String tokenHeaderName;

	private HeaderHttpSessionStrategy headerStrategy;
	private CookieHttpSessionStrategy cookieStrategy;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(strategyHeaderName, "strategyHeaderName must has text!");
		this.headerStrategy = new HeaderHttpSessionStrategy();
		this.headerStrategy.setHeaderName(tokenHeaderName);
		this.cookieStrategy = new CookieHttpSessionStrategy();
	}

	@Override
	public String getRequestedSessionId(HttpServletRequest request) {
		return getCurrentStrategy(request).getRequestedSessionId(request);
	}

	@Override
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		getCurrentStrategy(request).onNewSession(session, request, response);
	}

	@Override
	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		getCurrentStrategy(request).onInvalidateSession(request, response);
	}
	
	public HttpSessionStrategy getCurrentStrategy(HttpServletRequest request) {
		String header = request.getHeader(strategyHeaderName);
		if(SessionStrategies.HEADER.equals(header)) {
			return headerStrategy;
		}
		return cookieStrategy;
	}

}
