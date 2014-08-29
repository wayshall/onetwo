package org.onetwo.app.taskserver.service;

import org.onetwo.plugins.task.entity.TaskQueue;

public interface TaskExecuteListener {

	public Object execute(TaskQueue task);

}
