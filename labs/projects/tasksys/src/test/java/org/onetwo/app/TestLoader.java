package org.onetwo.app;

import org.onetwo.app.tasksys.TasksysConfig;
import org.onetwo.app.tasksys.TasksysContextConfig;
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
