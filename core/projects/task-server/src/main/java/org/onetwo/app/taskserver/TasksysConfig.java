package org.onetwo.app.taskserver;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class TasksysConfig extends AppConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(TasksysConfig.class);

	private static final TasksysConfig instance = new TasksysConfig();
	
	public static TasksysConfig getInstance() {
		return instance;
	}

	protected TasksysConfig() {
		super("siteConfig.properties");
	}
	
}
