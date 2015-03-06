package org.onetwo.common.spring.plugin;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public abstract class AbstractLoadingConfig implements LoadableConfig {
	private JFishProperties config;
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
		this.initConfig(config);
	}

	protected abstract void initConfig(JFishProperties config);
	
	@Override
	public JFishProperties getSourceConfig() {
		if(config==null){
			throw new BaseException("config is not load!");
		}
		return config;
	}
	

}
