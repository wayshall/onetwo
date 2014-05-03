package org.onetwo.common.spring.utils;

import java.io.IOException;

import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

public class JFishPropertiesFactoryBean extends PropertiesFactoryBean {

	protected JFishProperties mergeProperties() throws IOException {
		return new JFishProperties(super.mergeProperties());
	}
}
