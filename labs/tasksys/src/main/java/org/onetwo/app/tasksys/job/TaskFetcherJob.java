package org.onetwo.app.tasksys.job;

import javax.annotation.Resource;

import org.onetwo.app.tasksys.model.TaskType;
import org.onetwo.app.tasksys.model.entity.TaskQueue;
import org.onetwo.app.tasksys.model.service.impl.TaskProcessor;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.QuartzJobTask;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TaskFetcherJob implements QuartzJobTask {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private TaskProcessor taskWorkerAcotr;
	private int count;
	
	public void execute() {
		logger.info("execte test...");
		TaskQueue task = new TaskQueue();
		task.setType(TaskType.EMAIL.toString());
		task.setName("task-"+(count++));
		task.setCurrentTimes(count);
		this.taskWorkerAcotr.sendTask(task);
	}

	public String getCronExpression() {
		return "0/1 * * * * ?";
	}

}
