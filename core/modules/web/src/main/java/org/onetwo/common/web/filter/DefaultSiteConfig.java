package org.onetwo.common.web.filter;

import org.onetwo.common.convert.Types;
import org.onetwo.common.propconf.JFishProperties;

public class DefaultSiteConfig implements SiteConfig {
	
	final private JFishProperties config = new JFishProperties();
	
	public JFishProperties getConfig() {
		return config;
	}

	protected String getProperty(String key) {
		return config.getProperty(key);
	}

	protected String getProperty(String key, String def) {
		return getConfig(key, def);
	}

	@Override
	public String getConfig(String key, String def) {
		return config.getProperty(key, def);
	}

	@Override
	public <T> T getConfig(String key, T defValue, Class<T> valueType) {
		String valueString = config.getProperty(key);
		T val = Types.convertValue(valueString, valueType, defValue);
		return val;
	}
	
	

}
