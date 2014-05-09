package org.onetwo.app.task;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class TaskConfig extends AppConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(TaskConfig.class);

	private static final TaskConfig instance = new TaskConfig();
	
	public static TaskConfig getInstance() {
		return instance;
	}

	protected TaskConfig() {
		super("siteConfig.properties");
	}
	
	public int getQueueCapacity(){
		return getInt("queue.capacity", 1000);
	}
	
	public int getTaskConsumerCount(){
		return getInt("consumer.count", 1);
	}
	
	public String getDataSyncTaskCronExpression(){
		return getProperty("dataSyncTask.cronExpression");
	}
}
