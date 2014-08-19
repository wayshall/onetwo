package org.onetwo.app.tasksys.model.service;

import org.onetwo.app.tasksys.model.TaskData;
import org.onetwo.app.tasksys.model.TaskResult;

public interface TaskListener{
	
	public TaskResult execute(TaskData task);

}
