package org.onetwo.app.task.job;

import javax.annotation.Resource;

import org.onetwo.app.task.TaskQueue;
import org.onetwo.app.task.test.MyEmailInfo;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.JobTask;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DataSyncTask implements JobTask {
	private final Logger logger = MyLoggerFactory.getLogger(DataSyncTask.class);
	
	@Resource
	private TaskQueue taskQueue;
	
	public DataSyncTask(){
		logger.info("DataSyncTask");
	}

	@Override
	public void execute() {
		logger.info("execute...");
		JFishList.intList(0, 5).each(new NoIndexIt<Integer>() {

			@Override
			protected void doIt(Integer element) throws Exception {
				MyEmailInfo email = new MyEmailInfo();
				taskQueue.putTask(email);
			}
			
		});
	}

}
