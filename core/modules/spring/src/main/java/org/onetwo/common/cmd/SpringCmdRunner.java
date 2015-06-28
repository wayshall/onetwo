package org.onetwo.common.cmd;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.commandline.CmdRunner;
import org.onetwo.common.utils.commandline.CommandManager;
import org.onetwo.common.utils.commandline.DefaultCommandManager;
import org.onetwo.common.utils.commandline.HelpCommand;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

abstract public class SpringCmdRunner extends CmdRunner {
	
	protected void loadCommand(String[] args) {
		cmdManager = new DefaultCommandManager();
		cmdManager.addCommand(new SpringExitCommand());
		cmdManager.addCommand(new HelpCommand());
		
		loadUserCommand(cmdManager);
	}
	
	protected void loadUserCommand(CommandManager cmdManager){
	}
	
	abstract protected AbstractRefreshableConfigApplicationContext createApplicationContext();
	
	@Override
	protected void startAppContext(String[] args) {
//		SpringConfigApplicationContext context = new SpringConfigApplicationContext();
		AbstractRefreshableConfigApplicationContext context = createApplicationContext();
//		context.setConfigLocation("");
		SpringApplication.initApplication(context);
		initApplicationContext(context);
		context.refresh();
		this.afterInitApplicationContext(context);
	}
	
	protected void initApplicationContext(AbstractRefreshableConfigApplicationContext context){
	}
	
	protected void afterInitApplicationContext(AbstractRefreshableConfigApplicationContext context){
		SpringApplication.getInstance().printBeanNames();
	}

}
