package org.onetwo.plugins.activemq.consumer;

import org.onetwo.common.test.spring.SpringProfilesWebApplicationContextLoader;
import org.onetwo.plugins.activemq.test.ActivemqSiteConfig;

public class ActivemqConsumerLoader extends SpringProfilesWebApplicationContextLoader {

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{ActivemqConsumerTestContext.class};
//		return new Class<?>[]{PluginConsumerContext.class, ActivemqConsumerTestContext.class};
	}

	@Override
	protected String getAppEnvironment() {
		return ActivemqSiteConfig.getInstance().getAppEnvironment();
	}

}
