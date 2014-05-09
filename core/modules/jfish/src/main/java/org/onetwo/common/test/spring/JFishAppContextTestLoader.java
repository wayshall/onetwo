package org.onetwo.common.test.spring;


import org.onetwo.common.fish.spring.config.JFishContextConfig;
import org.onetwo.common.fish.web.JFishWebApplicationContext;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;

abstract public class JFishAppContextTestLoader extends AbstractContextLoader {

	abstract protected Class<?>[] getClassArray();
	
	public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
		Class<?>[] classes = getClassArray();
		classes = (Class<?>[])ArrayUtils.add(classes, JFishContextConfig.class);
		JFishWebApplicationContext context = new JFishWebApplicationContext(BaseSiteConfig.getInstance().getAppEnvironment(), classes);
		context.refresh();
		return context;
	}

	@Override
	public ApplicationContext loadContext(
			MergedContextConfiguration mergedConfig) throws Exception {
		JFishWebApplicationContext context = new JFishWebApplicationContext();
		context.refresh();
		return context;
	}

	@Override
	protected String getResourceSuffix() {
		return "-context-test.xml";
	}
	
}
