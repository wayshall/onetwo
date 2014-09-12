package org.onetwo.common.test.spring;


import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.common.utils.LangUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;

public class SpringConfigApplicationContextLoader extends AbstractContextLoader {

	protected Class<?>[] getClassArray(){ return null; };
	protected String getAppEnvironment(){ return "test"; };
	
	public final ConfigurableApplicationContext loadContext(String... locations) throws Exception {
		AbstractRefreshableConfigApplicationContext context = createContext();
		return context;
	}

	protected AbstractRefreshableConfigApplicationContext createContext(){
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
