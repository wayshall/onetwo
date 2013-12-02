package com.qyscard.dataexchange;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class WebConfig extends AppConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(WebConfig.class);

	private static final WebConfig instance = new WebConfig();
	
	public static WebConfig getInstance() {
		return instance;
	}

	protected WebConfig() {
		super("siteConfig.properties");
	}
	
}
