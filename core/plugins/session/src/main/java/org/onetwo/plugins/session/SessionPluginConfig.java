package org.onetwo.plugins.session;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class SessionPluginConfig implements LoadableConfig {

	private JFishProperties config;
	

	public SessionPluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
	}


	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}

}
