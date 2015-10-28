package org.onetwo.common.web.filter;

public interface SiteConfig {
	
	public String getConfig(String key, String def);
	public <T> T getConfig(String key, T defValue, Class<T> valueType);

}
