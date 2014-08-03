package org.onetwo.app.tasksys;

import org.onetwo.common.cmd.SpringCmdRunner;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringCmdRunner(){

			protected void initApplicationContext(SpringConfigApplicationContext context){
				context.setAppEnvironment(TasksysConfig.getInstance().getAppEnvironment());
				context.register(TasksysContextConfig.class);
			}
			
			
		}.run(args);
	}

}
