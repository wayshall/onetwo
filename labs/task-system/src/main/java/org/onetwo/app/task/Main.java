package org.onetwo.app.task;

import org.onetwo.common.cmd.SpringCmdRunner;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.common.utils.commandline.DefaultCommandManager;
import org.onetwo.common.utils.commandline.HelpCommand;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringCmdRunner(){

			@Override
			protected void loadCommand(String[] args) {
				cmdManager = new DefaultCommandManager();
				cmdManager.addCommand(new HelpCommand());
			}
			
			@Override
			protected void initApplicationContext(SpringConfigApplicationContext context){
				context.setAppEnvironment(TaskConfig.getInstance().getAppEnvironment());
				context.register(TaskContextConfig.class);
			}
			
			@Override
			protected void onRuning() {
				new MainTaskProcessor().doTask();
				super.onRuning();
			}
			
		}.run(args);
	}

}
