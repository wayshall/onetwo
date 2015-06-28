package org.onetwo.plugins.batch.cmd;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.cmd.SpringCmdRunner;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.context.SpringConfigApplicationContext;
import org.onetwo.common.utils.commandline.Command;
import org.onetwo.common.utils.commandline.CommandManager;
import org.springframework.batch.core.Job;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public class BatchCmdRunner extends SpringCmdRunner {
	
	protected void loadUserCommand(CommandManager cmdManager){
		Map<String, Job> jobs = SpringApplication.getInstance().getBeansMap(Job.class);
		for(Entry<String, Job> job : jobs.entrySet()){
			cmdManager.addCommand(createJobCommand(job.getKey(), job.getValue()));
			logger.info("load JobCommand : {} ", job.getKey());
		}
	}
	
	protected Command createJobCommand(String jobBeanName, Job job){
		return new JobCommand(jobBeanName);
	}

	@Override
    protected AbstractRefreshableConfigApplicationContext createApplicationContext() {
	    return new SpringConfigApplicationContext();
    }

}
