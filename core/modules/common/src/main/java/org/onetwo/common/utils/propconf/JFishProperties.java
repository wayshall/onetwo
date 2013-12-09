package org.onetwo.common.utils.propconf;


public class JFishProperties {

	private PropertiesWraper config;
	private PropertiesWraper properties;

	public PropertiesWraper getProperties() {
		return properties;
	}

	public void setProperties(PropertiesWraper propertiesWraper) {
		this.properties = propertiesWraper;
	}

	public PropertiesWraper getConfig() {
		return config;
	}

	public void setConfig(PropertiesWraper config) {
		this.config = config;
	}
}
