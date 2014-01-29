package org.onetwo.plugins.batch.cmd;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.commandline.AbstractCommand;
import org.onetwo.common.utils.commandline.CmdContext;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

public class JobCommand extends AbstractCommand {

	private static final Logger logger = MyLoggerFactory.getLogger(JobCommand.class);
	
	public JobCommand(String key) {
		super(key);
	}
	
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
		String runTimeString = inputRunTime(context);
		return new JobParametersBuilder()
				.addString("runTime", runTimeString)
				.toJobParameters();
	}
	
	protected String inputRunTime(CmdContext context) throws IOException{
		String runTimeString;
		while(true){
			System.out.print("输入任务的执行时间（ yyyy-MM-dd HH:mm:ss 当前时间回车即可）  > ");
			runTimeString = context.getCmdInput().readLine();
			if(StringUtils.isBlank(runTimeString)){
				runTimeString = DateUtil.formatDateTime(new Date());
				break;
			}else{
				Date runTime = DateUtil.parseDateTime(runTimeString);
				if(runTime!=null)
					break;
			}
		}
		return runTimeString;
	}
}
