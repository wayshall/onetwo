package org.onetwo.app.taskserver.job;

import javax.annotation.Resource;

import org.onetwo.app.taskserver.service.impl.DefaultTaskProcessor;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.QuartzJobTask;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskType;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TaskFetcherJob implements QuartzJobTask {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private DefaultTaskProcessor taskWorkerAcotr;
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
