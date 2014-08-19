package org.onetwo.app.tasksys.model.service;

import java.util.List;

import org.onetwo.app.tasksys.model.TaskType;

public interface TaskListenerManager {

	public abstract TaskListenerManager add(TaskType type,
			TaskListener taskExecutor);
	
	public List<TaskListener> getTaskListeners(TaskType type);

}