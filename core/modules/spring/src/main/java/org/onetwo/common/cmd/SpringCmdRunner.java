package org.onetwo.common.cmd;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.common.utils.commandline.CmdRunner;
import org.onetwo.common.utils.commandline.CommandManager;
import org.onetwo.common.utils.commandline.DefaultCommandManager;
import org.onetwo.common.utils.commandline.HelpCommand;

public class SpringCmdRunner extends CmdRunner {
	
	protected void loadCommand(String[] args) {
		cmdManager = new DefaultCommandManager();
		cmdManager.addCommand(new SpringExitCommand());
		cmdManager.addCommand(new HelpCommand());
		
		loadUserCommand(cmdManager);
	}
	
	protected void loadUserCommand(CommandManager cmdManager){
	}
	
	@Override
	protected void startAppContext(String[] args) {
		SpringConfigApplicationContext context = new SpringConfigApplicationContext();
//		context.setConfigLocation("");
		initApplicationContext(context);
		context.refresh();
		SpringApplication.initApplication(context);
		this.afterInitApplicationContext(context);
	}
	
	protected void initApplicationContext(SpringConfigApplicationContext context){
	}
	
	protected void afterInitApplicationContext(SpringConfigApplicationContext context){
		SpringApplication.getInstance().printBeanNames();
	}

}
