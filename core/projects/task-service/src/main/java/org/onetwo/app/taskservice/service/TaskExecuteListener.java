package org.onetwo.app.taskservice.service;

import org.onetwo.plugins.task.entity.TaskQueue;

public interface TaskExecuteListener extends TaskTypeMapper {
	
	
	public String getListenerName();

	public Object execute(TaskQueue task) throws Exception;

}
