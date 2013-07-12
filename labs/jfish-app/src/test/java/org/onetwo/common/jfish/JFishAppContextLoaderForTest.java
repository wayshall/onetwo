package org.onetwo.common.jfish;


import org.example.app.AppPackageRoot;
import org.onetwo.common.CommonPackageRoot;
import org.onetwo.common.fish.web.JFishWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;

public class JFishAppContextLoaderForTest extends AbstractContextLoader {

	public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
		JFishWebApplicationContext context = new JFishWebApplicationContext(new Class<?>[]{AppPackageRoot.class, CommonPackageRoot.class});
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
