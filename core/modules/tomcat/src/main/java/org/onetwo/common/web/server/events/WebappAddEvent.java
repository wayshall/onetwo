package org.onetwo.common.web.server.events;

import javax.servlet.ServletException;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class WebappAddEvent {
	
	private final TomcatServer tomcatServer;

	public WebappAddEvent(TomcatServer tomcatServer) {
		super();
		this.tomcatServer = tomcatServer;
	}

	public TomcatServer getTomcatServer() {
		return tomcatServer;
	}

	public TomcatServer getEventSource() {
		return tomcatServer;
	}
	
	public WebappAddEvent addWebapp(String contextPath, String baseDir){
		try {
			tomcatServer.getTomcat().addWebapp(contextPath, baseDir);
		} catch (ServletException e) {
			throw new BaseException("add webapp to tomcat error: " + e.getMessage(), e);
		}
		return this;
	}

}
