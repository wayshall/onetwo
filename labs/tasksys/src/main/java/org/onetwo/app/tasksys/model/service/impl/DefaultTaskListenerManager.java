package org.onetwo.app.tasksys.model.service.impl;

import java.util.List;

import org.onetwo.app.tasksys.model.TaskType;
import org.onetwo.app.tasksys.model.service.TaskCompleteListener;
import org.onetwo.app.tasksys.model.service.TaskExecuteListener;
import org.onetwo.app.tasksys.model.service.TaskListenerManager;
import org.onetwo.common.utils.map.ListMap;
import org.springframework.stereotype.Component;

@Component
public class DefaultTaskListenerManager implements TaskListenerManager {

	private ListMap<TaskType, TaskExecuteListener<?>> taskExecMapper = ListMap.newListMap(); 
	private ListMap<TaskType, TaskCompleteListener> taskCompleteMapper = ListMap.newListMap(); 

	@Override
	public TaskListenerManager addTaskExecuteListener(TaskType type, TaskExecuteListener<?> taskExecutor){
		this.taskExecMapper.putElement(type, taskExecutor);
		return this;
	}
	
	@Override
	public List<TaskExecuteListener<?>> getTaskExecuteListeners(TaskType type){
		return taskExecMapper.get(type);
	}
	
	@Override
	public TaskListenerManager addTaskCompleteListener(TaskType type, TaskCompleteListener taskCompletor){
		this.taskCompleteMapper.putElement(type, taskCompletor);
		return this;
	}
	
	@Override
	public List<TaskCompleteListener> getTaskCompleteListeners(TaskType type){
		return taskCompleteMapper.get(type);
	}

}
