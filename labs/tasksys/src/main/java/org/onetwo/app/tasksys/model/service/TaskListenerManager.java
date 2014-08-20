package org.onetwo.app.tasksys.model.service;

import java.util.List;

import org.onetwo.app.tasksys.model.TaskType;

public interface TaskListenerManager {

	public abstract TaskListenerManager addTaskExecuteListener(TaskType type,
			TaskExecuteListener<?> taskExecutor);
	
	public List<TaskExecuteListener<?>> getTaskExecuteListeners(TaskType type);
	

	public TaskListenerManager addTaskCompleteListener(TaskType type, TaskCompleteListener taskCompletor);
	
	public List<TaskCompleteListener> getTaskCompleteListeners(TaskType type);

}