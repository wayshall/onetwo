package org.onetwo.common.spring.plugin;

import javax.annotation.Resource;

import org.onetwo.common.utils.propconf.AppConfig;


abstract public class AbstractPluginContext {
	
	public static final String CONFIG_POSTFIX = ".properties";
	

	@Resource
	private AppConfig appConfig;


	public AppConfig getAppConfig() {
		return appConfig;
	}
	
}
