package org.onetwo.plugins.task.service;

import org.onetwo.plugins.task.utils.TaskData;

public interface TaskExecuteListener {

	public Object execute(TaskData task);

}
