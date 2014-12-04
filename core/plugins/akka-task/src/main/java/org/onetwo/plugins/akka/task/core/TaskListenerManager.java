package org.onetwo.plugins.akka.task.core;

import java.util.List;

public interface TaskListenerManager {

	public abstract TaskListenerManager addTaskExecuteListener(TaskType type,
			TaskExecuteListener taskExecutor);
	
	public List<TaskExecuteListener> getTaskExecuteListeners(TaskType type);
	

	public TaskListenerManager addTaskCompleteListener(TaskType type, TaskCompleteListener taskCompletor);
	
	public List<TaskCompleteListener> getTaskCompleteListeners(TaskType type);

}