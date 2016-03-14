package org.onetwo.common.spring.utils;

import java.io.IOException;

import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.utils.Assert;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

public class JFishPropertiesFactoryBean extends PropertiesFactoryBean {
	
	private final JFishProperties jfishProperties;
	
	public JFishPropertiesFactoryBean(JFishProperties jfishProperties) {
		super();
		Assert.notNull(jfishProperties);
		this.jfishProperties = jfishProperties;
	}


	protected JFishProperties mergeProperties() throws IOException {
		jfishProperties.setConfigs(super.mergeProperties());
		return jfishProperties;
	}
}
