package org.onetwo.app.tasksys.job;

import javax.annotation.Resource;

import org.onetwo.app.tasksys.model.entity.TaskQueue;
import org.onetwo.app.tasksys.model.service.impl.TaskWorkerAcotr;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.QuartzJobTask;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TaskFetcherJob implements QuartzJobTask {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private TaskWorkerAcotr taskWorkerImpl;
	private int count;
	
	public void execute() {
		logger.info("execte test...");
		TaskQueue task = new TaskQueue();
		task.setName("task-"+(count++));
		task.setCurrentTimes(count);
		this.taskWorkerImpl.sendTask(task);
	}

	public String getCronExpression() {
		return "0/1 * * * * ?";
	}

}
