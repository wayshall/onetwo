package org.onetwo.project.batch.test;

import java.util.Date;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.common.utils.commandline.AbstractCommand;
import org.onetwo.common.utils.commandline.CmdContext;
import org.onetwo.common.utils.commandline.CmdRunner;
import org.onetwo.common.utils.commandline.DefaultCommandManager;
import org.onetwo.common.utils.commandline.ExitCommand;
import org.onetwo.common.utils.commandline.HelpCommand;
import org.onetwo.project.batch.BatchConfig;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;


public class TestMain {
	
	private static final Logger logger = MyLoggerFactory.getLogger(TestMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CmdRunner(){

			protected void loadCommand(String[] args) {
				cmdManager = new DefaultCommandManager();
				cmdManager.addCommand(new ExitCommand());
				cmdManager.addCommand(new HelpCommand());
				cmdManager.addCommand(new AbstractCommand("importTest") {
					
					@Override
					public void doExecute(CmdContext context) {
						JobLauncher jobLauncher = SpringApplication.getInstance().getBean(JobLauncher.class);
						try {
							jobLauncher.run(SpringApplication.getInstance().getBean(Job.class, "importDatas"), 
												new JobParametersBuilder()
													.addDate("runDate", new Date())
													.toJobParameters());
						} catch (Exception e) {
							logger.error("import data error: " + e.getMessage(), e);
						}
						
					}
				});
			}
			
			@Override
			protected void startAppContext(String[] args) {
				//SpringProfilesWebApplicationContext
//				SpringUtils.setProfiles(BatchConfig.getInstance().getAppEnvironment());
//				ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
				SpringConfigApplicationContext context = new SpringConfigApplicationContext();
				context.setAppEnvironment(BatchConfig.getInstance().getAppEnvironment());
				context.register(TestBatchContextConfig.class);
//				context.setConfigLocation("");
				context.refresh();
				SpringApplication.initApplication(context);
				
			}
			
		}.run(args);
	}

}
