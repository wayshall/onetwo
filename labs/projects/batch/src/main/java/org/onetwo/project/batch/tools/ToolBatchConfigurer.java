package org.onetwo.project.batch.tools;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;

public class ToolBatchConfigurer implements BatchConfigurer{

	public JobRepository getJobRepository() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public PlatformTransactionManager getTransactionManager() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public JobLauncher getJobLauncher() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
