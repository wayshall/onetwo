package org.onetwo.common.web.server.event.impl;

import org.onetwo.common.web.server.event.AfterWebappAddEvent;
import org.onetwo.common.web.server.tomcat.TomcatServer;

public class DefaultAfterWebappAddEvent implements AfterWebappAddEvent<TomcatServer> {
	
	private final TomcatServer tomcatServer;

	public DefaultAfterWebappAddEvent(TomcatServer tomcatServer) {
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
	

}
