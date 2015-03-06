package org.onetwo.app;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;

import apptimer.TimerConfig;
import apptimer.TimerContextConfig;

public class TestLoader extends SpringConfigApplicationContextLoader {

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{TimerContextConfig.class};
	}

	@Override
	protected String getAppEnvironment() {
		return TimerConfig.getInstance().getAppEnvironment();
	}
	
	

}
