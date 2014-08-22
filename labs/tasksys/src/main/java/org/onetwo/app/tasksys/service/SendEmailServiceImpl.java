package org.onetwo.app.tasksys.service;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.plugins.task.service.TaskExecuteListener;
import org.onetwo.plugins.task.service.TaskTypeMapper;
import org.onetwo.plugins.task.utils.TaskData;
import org.onetwo.plugins.task.utils.TaskType;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements TaskExecuteListener, TaskTypeMapper {
	final private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Override
	public TaskType[] getListenerMappedTaskTypes() {
		return new TaskType[]{TaskType.EMAIL};
	}

	@Override
	public Object execute(TaskData task) {
		logger.info("execute email task ");
		return null;
	}
	

}
