package org.onetwo.plugins.batch.cmd;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.cmd.SpringCmdRunner;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.commandline.CommandManager;
import org.springframework.batch.core.Job;

public class BatchCmdRunner extends SpringCmdRunner {
	
	protected void loadUserCommand(CommandManager cmdManager){
		Map<String, Job> jobs = SpringApplication.getInstance().getBeansMap(Job.class);
		for(Entry<String, Job> job : jobs.entrySet()){
			cmdManager.addCommand(new JobCommand(job.getKey()));
			logger.info("load JobCommand : {} ", job.getKey());
		}
	}

}
