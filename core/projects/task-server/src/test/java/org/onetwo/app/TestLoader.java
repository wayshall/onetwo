package org.onetwo.app;

import org.onetwo.app.taskserver.TasksysConfig;
import org.onetwo.app.taskserver.TasksysContextConfig;
import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;

public class TestLoader extends SpringConfigApplicationContextLoader {

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{TasksysContextConfig.class};
	}

	@Override
	protected String getAppEnvironment() {
		return TasksysConfig.getInstance().getAppEnvironment();
	}
	
	

}
