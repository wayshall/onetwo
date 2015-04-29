package org.onetwo.app;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;

import org.onetwo.test.dbtest.TimerConfig;
import org.onetwo.test.dbtest.TimerContextConfig;

public class DBTestLoader extends SpringConfigApplicationContextLoader {

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{TimerContextConfig.class};
	}

	@Override
	protected String getAppEnvironment() {
		return TimerConfig.getInstance().getAppEnvironment();
	}
	
	

}
