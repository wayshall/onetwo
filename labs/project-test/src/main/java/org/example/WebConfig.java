package org.example;

import org.onetwo.common.web.config.BaseSiteConfig;


public class WebConfig {
	private static final WebConfig instance;
	
	static {
		instance = new WebConfig();
		instance.siteConfig = BaseSiteConfig.getInstance();
	}
	
	public static WebConfig getInstance(){
		return instance;
	}
	
	private BaseSiteConfig siteConfig;
	
	private WebConfig(){
	}

	public BaseSiteConfig getSiteConfig() {
		return siteConfig;
	}
	
}
