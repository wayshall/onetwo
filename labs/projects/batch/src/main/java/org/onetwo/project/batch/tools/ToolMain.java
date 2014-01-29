package org.onetwo.project.batch.tools;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.plugins.batch.cmd.BatchCmdRunner;
import org.onetwo.project.batch.BatchConfig;
import org.slf4j.Logger;


public class ToolMain {
	
	private static final Logger logger = MyLoggerFactory.getLogger(ToolMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BatchCmdRunner(){

			protected void initApplicationContext(SpringConfigApplicationContext context){
				context.setAppEnvironment(BatchConfig.getInstance().getAppEnvironment());
				context.register(ToolBatchContextConfig.class);
			}
			
		}.run(args);
	}

}
