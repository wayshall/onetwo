package org.onetwo.plugins.activemq.producer;

import org.onetwo.common.test.spring.SpringProfilesWebApplicationContextLoader;
import org.onetwo.plugins.activemq.test.ActivemqSiteConfig;

public class ActivemqProducerLoader extends SpringProfilesWebApplicationContextLoader {

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{ActivemqProducerTestContext.class};
//		return new Class<?>[]{PluginProducerContext.class, ActivemqProducerTestContext.class};
	}

	@Override
	protected String getAppEnvironment() {
		return ActivemqSiteConfig.getInstance().getAppEnvironment();
	}

}
