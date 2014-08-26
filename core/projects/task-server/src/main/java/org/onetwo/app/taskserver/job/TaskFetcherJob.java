package org.onetwo.app.taskserver.job;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.app.taskserver.TaskServerConfig;
import org.onetwo.app.taskserver.service.impl.DefaultTaskProcessor;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.timer.QuartzJobTask;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class TaskFetcherJob implements QuartzJobTask, InitializingBean {

	private Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private DefaultTaskProcessor taskProcessor;
	@Resource
	private TaskQueueServiceImpl taskQueueService;
	@Resource
	private TaskServerConfig taskServerConfig;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<TaskQueue> executing = taskQueueService.loadAllExecuting();
		for(TaskQueue tq : executing){
			this.taskProcessor.sendTask(tq);
		}
		logger.info("loaded {} queued task on startup.", executing.size());
	}

	public void execute() {
		if(taskProcessor.isFull()){
			logger.info("queue of processor is full, ignore……");
			return ;
		}
		List<TaskQueue> taskQueues = taskQueueService.loadAndLockWaiting(taskServerConfig.getFetchSize());
		for(TaskQueue tq : taskQueues){
			this.taskProcessor.sendTask(tq);
		}
		logger.info("send {} waiting task to processor. ", taskQueues.size());
	}

	public String getCronExpression() {
		return "0/10 * * * * ?";
	}

}
