package org.onetwo.common.test.spring;


import org.onetwo.common.fish.web.JFishWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;

abstract public class JFishAppContextTestLoader extends AbstractContextLoader {

	abstract protected Class<?>[] getClassArray();
	
	public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
		JFishWebApplicationContext context = new JFishWebApplicationContext(getClassArray());
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
