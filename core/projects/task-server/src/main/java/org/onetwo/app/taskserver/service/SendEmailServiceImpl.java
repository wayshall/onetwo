package org.onetwo.app.taskserver.service;

import javax.annotation.Resource;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.plugins.email.JavaMailService;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.onetwo.plugins.task.utils.TaskType;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements TaskExecuteListener, TaskTypeMapper {
	final private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Resource
	private TaskQueueServiceImpl taskQueueService;
	
	@Resource
	private JavaMailService javaMailService;
	
	@Override
	public TaskType[] getListenerMappedTaskTypes() {
		return new TaskType[]{TaskType.EMAIL};
	}

	@Override
	public Object execute(TaskQueue taskQueue) {
		logger.info("execute email task : {}", taskQueue.getName());
		boolean succeed = true;
		try {
			this.javaMailService.send(null);
		} catch (Exception e) {
			succeed = false;
		}
		this.taskQueueService.archived(taskQueue, succeed);
		return null;
	}
	

}
