package org.onetwo.app.tasksys.job;

import javax.annotation.Resource;

import org.onetwo.app.tasksys.model.entity.TaskQueueEntity;
import org.onetwo.app.tasksys.model.service.TaskWorkerImpl;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.QuartzJobTask;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TaskFetcherJob implements QuartzJobTask {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private TaskWorkerImpl taskWorkerImpl;
	private int count;
	
	public void execute() {
		logger.info("execte test...");
		TaskQueueEntity task = new TaskQueueEntity();
		task.setName("task-"+(count++));
		task.setCurrentTimes(count);
		this.taskWorkerImpl.addTask(task);
	}

	public String getCronExpression() {
		return "0/1 * * * * ?";
	}

}
