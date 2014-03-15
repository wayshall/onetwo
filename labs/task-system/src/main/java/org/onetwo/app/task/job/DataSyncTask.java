package org.onetwo.app.task.job;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.JobTask;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DataSyncTask implements JobTask {
	private final Logger logger = MyLoggerFactory.getLogger(DataSyncTask.class);
	
	public DataSyncTask(){
		logger.info("DataSyncTask");
	}

	@Override
	public void execute() {
		
		logger.info("execute...");
		
	}

}
