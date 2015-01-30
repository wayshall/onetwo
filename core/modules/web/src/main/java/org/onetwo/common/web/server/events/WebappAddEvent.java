package org.onetwo.common.web.server.events;

import org.apache.catalina.startup.Tomcat;

public class WebappAddEvent {
	
	private final Tomcat tomcat;

	public WebappAddEvent(Tomcat tomcat) {
		super();
		this.tomcat = tomcat;
	}

	public Tomcat getTomcat() {
		return tomcat;
	}
	

}
