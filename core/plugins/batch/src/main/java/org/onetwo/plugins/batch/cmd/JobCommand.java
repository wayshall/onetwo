package org.onetwo.plugins.batch.cmd;

import java.util.Date;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.commandline.AbstractCommand;
import org.onetwo.common.utils.commandline.CmdContext;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

public class JobCommand extends AbstractCommand {

	public JobCommand(String key) {
		super(key);
	}

	private static final Logger logger = MyLoggerFactory.getLogger(JobCommand.class);
	
	@Override
	public void doExecute(CmdContext context) {
		JobLauncher jobLauncher = SpringApplication.getInstance().getBean(JobLauncher.class);
		try {
			Job job = SpringApplication.getInstance().getBean(Job.class, getKey());
			jobLauncher.run(job, buildJobParameter(context));
		} catch (Exception e) {
			handleException(e);
		}
		
	}
	
	protected void handleException(Exception e){
		logger.error("{} error: " + e.getMessage(), getKey(), e);
	}
	
	protected JobParameters buildJobParameter(CmdContext context) throws Exception {
		return new JobParametersBuilder()
				.addDate("runDate", new Date())
				.toJobParameters();
	}
}
