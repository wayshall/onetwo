package org.onetwo.plugins.activemq.test;

import org.onetwo.common.utils.propconf.AppConfig;

public class ActivemqSiteConfig extends AppConfig{
	
	private static final ActivemqSiteConfig instance = new ActivemqSiteConfig();
	
	

	public static ActivemqSiteConfig getInstance() {
		return instance;
	}

	protected ActivemqSiteConfig() {
		super("siteConfig");
	}

}
