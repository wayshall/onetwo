package org.onetwo.test.jorm;

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

			protected void loadCommand(String[] args) {
				cmdManager = new DefaultCommandManager();
				cmdManager.addCommand(new HelpCommand());
			}
			
			protected void initApplicationContext(SpringConfigApplicationContext context){
				context.setAppEnvironment(JFishOrmConfig.getInstance().getAppEnvironment());
				context.register(JFishOrmContextConfig.class);
			}
			
			
		}.run(args);
	}

}
