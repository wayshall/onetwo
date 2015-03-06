package org.onetwo.app.taskservice;

import org.onetwo.common.cmd.SpringCmdRunner;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;

public class TaskService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringCmdRunner(){

			protected void initApplicationContext(SpringConfigApplicationContext context){
				context.setAppEnvironment(TaskServerConfig.getInstance().getAppEnvironment());
				context.register(TasksysContextConfig.class);
			}

			protected void afterInitApplicationContext(SpringConfigApplicationContext context){
				super.afterInitApplicationContext(context);
				
			}

		}.run(args);
	}

}
