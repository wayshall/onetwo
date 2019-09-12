package org.onetwo.boot.module.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.module.session.SessionProperties.SessionStrategies;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.util.Assert;

import lombok.Setter;

/**
 * @author weishao zeng
 * <br/>
 */
public class CustomizableHttpSessionStrategy implements HttpSessionIdResolver, InitializingBean {
	@Setter
	private String strategyHeaderName;
	@Setter
	private String tokenHeaderName;

	private HeaderHttpSessionIdResolver headerStrategy;
	private CookieHttpSessionIdResolver cookieStrategy;
	
	@Autowired(required=false)
	private CookieSerializer cookieSerializer;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(strategyHeaderName, "strategyHeaderName must has text!");
		this.headerStrategy = new HeaderHttpSessionIdResolver(tokenHeaderName);
//		this.headerStrategy.setHeaderName(tokenHeaderName);
		this.cookieStrategy = new CookieHttpSessionIdResolver();
		if (cookieSerializer!=null) {
			this.cookieStrategy.setCookieSerializer(cookieSerializer);
		}
	}

	@Override
	public List<String> resolveSessionIds(HttpServletRequest request) {
		return getCurrentStrategy(request).resolveSessionIds(request);
	}

	@Override
	public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
		getCurrentStrategy(request).setSessionId(request, response, sessionId);
	}

	@Override
	public void expireSession(HttpServletRequest request, HttpServletResponse response) {
		getCurrentStrategy(request).expireSession(request, response);
	}
	
	public HttpSessionIdResolver getCurrentStrategy(HttpServletRequest request) {
		String header = request.getHeader(strategyHeaderName);
		SessionStrategies strategy = SessionStrategies.of(header);
		if(SessionStrategies.HEADER.equals(strategy)) {
			return headerStrategy;
		}
		return cookieStrategy;
	}

}
