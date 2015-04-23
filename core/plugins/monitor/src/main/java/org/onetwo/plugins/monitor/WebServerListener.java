package org.onetwo.plugins.monitor;

import org.onetwo.common.web.server.events.AfterWebappAddEvent;
import org.onetwo.common.web.server.tomcat.EmbeddedTomcatListener;

import com.google.common.eventbus.Subscribe;

public class WebServerListener implements EmbeddedTomcatListener {
	
	@Subscribe
	public void lister(AfterWebappAddEvent event ){
		//http://hawt.io/configuration/index.html
		System.setProperty("hawtio.authenticationEnabled", "false");
	}

}
