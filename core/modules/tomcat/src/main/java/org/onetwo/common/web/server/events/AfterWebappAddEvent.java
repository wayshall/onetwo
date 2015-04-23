package org.onetwo.common.web.server.events;

import org.onetwo.common.web.server.tomcat.TomcatServer;

public class AfterWebappAddEvent {
	
	private final TomcatServer tomcatServer;

	public AfterWebappAddEvent(TomcatServer tomcatServer) {
		super();
		this.tomcatServer = tomcatServer;
	}

	public TomcatServer getTomcatServer() {
		return tomcatServer;
	}

	public TomcatServer getEventSource() {
		return tomcatServer;
	}
	

}
