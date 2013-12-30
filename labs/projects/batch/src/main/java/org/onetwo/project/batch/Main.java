package org.onetwo.project.batch;

import java.util.Date;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.commandline.CmdRunner;
import org.onetwo.common.utils.commandline.DefaultCommandManager;
import org.onetwo.common.utils.commandline.ExitCommand;
import org.onetwo.common.utils.commandline.HelpCommand;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	
	private static final Logger logger = MyLoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CmdRunner(){

			protected void loadCommand(String[] args) {
				cmdManager = new DefaultCommandManager();
				cmdManager.addCommand(new ExitCommand());
				cmdManager.addCommand(new HelpCommand());
			}
			
			@Override
			protected void startAppContext(String[] args) {
				//SpringProfilesWebApplicationContext
				SpringUtils.setProfiles(BatchConfig.getInstance().getAppEnvironment());
				ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
				SpringApplication.initApplication(context);
				
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
			
		}.run(args);
	}

}
