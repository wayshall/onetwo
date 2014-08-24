package org.onetwo.app.taskserver.service;

import org.onetwo.plugins.task.utils.TaskData;

public interface TaskExecuteListener {

	public Object execute(TaskData task);

}
