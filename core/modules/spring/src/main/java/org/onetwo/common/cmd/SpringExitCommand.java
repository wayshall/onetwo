package org.onetwo.common.cmd;

import org.onetwo.common.commandline.CmdContext;
import org.onetwo.common.commandline.ExitCommand;
import org.onetwo.common.spring.Springs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringExitCommand extends ExitCommand {
	@Override
	public void doExecute(CmdContext context) {
		System.out.println("shutdown.....");
		ConfigurableApplicationContext app = Springs.getInstance().getConfigurableAppContext();
		this.destroyApplication(app);
		if(app!=null){
			app.close();
		}
		System.exit(0);
	}
	
	protected void destroyApplication(ApplicationContext appContext){
	}
}
