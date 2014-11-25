package org.onetwo.app.taskservice.service;

import org.onetwo.plugins.task.entity.TaskQueue;

public interface TaskExecuteListener {

	public Object execute(TaskQueue task);

}
