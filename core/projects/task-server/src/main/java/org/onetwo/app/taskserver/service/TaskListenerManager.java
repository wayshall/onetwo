package org.onetwo.app.taskserver.service;

import java.util.List;

import org.onetwo.plugins.task.utils.TaskType;

public interface TaskListenerManager {

	public abstract TaskListenerManager addTaskExecuteListener(TaskType type,
			TaskExecuteListener taskExecutor);
	
	public List<TaskExecuteListener> getTaskExecuteListeners(TaskType type);
	

	public TaskListenerManager addTaskCompleteListener(TaskType type, TaskCompleteListener taskCompletor);
	
	public List<TaskCompleteListener> getTaskCompleteListeners(TaskType type);

}