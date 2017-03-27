package org.onetwo.common.cmd;

import org.onetwo.common.commandline.CmdRunner;
import org.onetwo.common.commandline.CommandManager;
import org.onetwo.common.commandline.DefaultCommandManager;
import org.onetwo.common.commandline.HelpCommand;
import org.onetwo.common.spring.Springs;
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
		Springs.initApplicationIfNotInitialized(context);
		initApplicationContext(context);
		context.refresh();
		this.afterInitApplicationContext(context);
	}
	
	protected void initApplicationContext(AbstractRefreshableConfigApplicationContext context){
	}
	
	protected void afterInitApplicationContext(AbstractRefreshableConfigApplicationContext context){
		Springs.getInstance().printBeanNames();
	}

}
