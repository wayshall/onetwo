package org.onetwo.plugins.monitor;

import org.onetwo.common.web.server.events.WebappAddEvent;

import com.google.common.eventbus.Subscribe;

public class WebServerListener {
	
	@Subscribe
	public void lister(WebappAddEvent event ){
		System.setProperty("hawtio.authenticationEnabled", "false");
	}

}
