package org.onetwo.plugins.security.server;

import org.onetwo.common.utils.propconf.JFishProperties;

public class SsoServerConfig extends JFishProperties {

	public String getService(String name){
		return getProperty("services."+name);
	}
}
