package org.onetwo.common.test.spring;


import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;

abstract public class SpringProfilesWebApplicationContextLoader extends AbstractContextLoader {

	abstract protected Class<?>[] getClassArray();
	abstract protected String getAppEnvironment();
	
	public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
		SpringProfilesWebApplicationContext context = createContext();
		return context;
	}

	protected SpringProfilesWebApplicationContext createContext(){
		SpringProfilesWebApplicationContext context = new SpringProfilesWebApplicationContext();
		context.setAppEnvironment(getAppEnvironment());
		context.setAnnotatedClasses(getClassArray());
		context.refresh();
		return context;
	}
	
	@Override
	public ApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {
		return createContext();
	}

	@Override
	protected String getResourceSuffix() {
		return "-context-test.xml";
	}
	
}
