package org.onetwo.common.test.spring;


import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.common.utils.LangUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;

abstract public class SpringConfigApplicationContextLoader extends AbstractContextLoader {

	abstract protected Class<?>[] getClassArray();
	abstract protected String getAppEnvironment();
	
	public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
		SpringConfigApplicationContext context = createContext();
		return context;
	}

	protected SpringConfigApplicationContext createContext(){
		SpringConfigApplicationContext context = new SpringConfigApplicationContext();
		context.setAppEnvironment(getAppEnvironment());
		if(!LangUtils.isEmpty(getClassArray()))
			context.register(getClassArray());
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
