package org.onetwo.common.web.server.event.impl;

import javax.servlet.ServletException;

import org.onetwo.common.web.server.event.WebappAddEvent;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class DefaultWebappAddEvent implements WebappAddEvent<TomcatServer> {
	
	private final TomcatServer tomcatServer;

	public DefaultWebappAddEvent(TomcatServer tomcatServer) {
		super();
		this.tomcatServer = tomcatServer;
	}

	public TomcatServer getTomcatServer() {
		return tomcatServer;
	}

	@Override
	public TomcatServer getEventSource() {
		return tomcatServer;
	}
	
	@Override
	public WebappAddEvent<TomcatServer> addWebapp(String contextPath, String baseDir){
		try {
			tomcatServer.getTomcat().addWebapp(contextPath, baseDir);
		} catch (ServletException e) {
			throw new RuntimeException("add webapp to tomcat error: " + e.getMessage(), e);
		}
		return this;
	}

}
