package com.qyscard.task;

import org.onetwo.app.task.MainTaskProcessor;
import org.onetwo.app.task.TaskConfig;
import org.onetwo.common.cmd.SpringCmdRunner;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.common.utils.commandline.DefaultCommandManager;
import org.onetwo.common.utils.commandline.HelpCommand;
import org.slf4j.Logger;

import com.qyscard.task.utils.TaskContextConfig;


public class Main {

	private static final Logger logger = MyLoggerFactory.getLogger(Main.class);
	
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
				final MainTaskProcessor processor = SpringApplication.getInstance().getBean(MainTaskProcessor.class);
				processor.doProcess();
				super.onRuning();
			}
			
		}.run(args);
	}

}
