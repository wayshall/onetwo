package org.onetwo.plugins.monitor;

import org.onetwo.common.web.server.event.AfterWebappAddEvent;
import org.onetwo.common.web.server.listener.EmbeddedServerListener;

import com.google.common.eventbus.Subscribe;

public class WebServerListener implements EmbeddedServerListener {
	
	@Subscribe
	public void lister(AfterWebappAddEvent<?> event ){
		//http://hawt.io/configuration/index.html
		System.setProperty("hawtio.authenticationEnabled", "false");
	}

}
