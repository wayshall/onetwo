package org.onetwo.common.fish.plugin;

import javax.annotation.Resource;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.AppConfig;


abstract public class AbstractPluginContext {
	
	public static final String CONFIG_POSTFIX = ".properties";
	

	@Resource
	private AppConfig appConfig;

	private final String configBaseDir;
	private final String configName;
	
	protected AbstractPluginContext(String configBaseDir, String configName) {
		super();
		this.configBaseDir = configBaseDir;
		this.configName = configName;
	}


	public AppConfig getAppConfig() {
		return appConfig;
	}

	protected String getConfigPath(){
		return LangUtils.appendNotBlank(configBaseDir, configName, CONFIG_POSTFIX);
	}
	protected String getEnvConfigPath(){
		return LangUtils.appendNotBlank(configBaseDir, configName, "-", appConfig.getAppEnvironment(), CONFIG_POSTFIX);
	}
	
}
