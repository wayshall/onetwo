package org.onetwo.common.cmd;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.commandline.CmdContext;
import org.onetwo.common.utils.commandline.ExitCommand;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringExitCommand extends ExitCommand {
	@Override
	public void doExecute(CmdContext context) {
		System.out.println("shutdown.....");
		ConfigurableApplicationContext app = SpringApplication.getInstance().getConfigurableAppContext();
		if(app!=null){
			app.close();
		}
		System.exit(0);
	}
}
