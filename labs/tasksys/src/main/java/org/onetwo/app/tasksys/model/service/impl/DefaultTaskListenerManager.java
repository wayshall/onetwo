package org.onetwo.app.tasksys.model.service.impl;

import java.util.List;

import org.onetwo.app.tasksys.model.TaskType;
import org.onetwo.app.tasksys.model.service.TaskListener;
import org.onetwo.app.tasksys.model.service.TaskListenerManager;
import org.onetwo.common.utils.map.ListMap;

public class DefaultTaskListenerManager implements TaskListenerManager {
	
	private ListMap<TaskType, TaskListener> mapper = ListMap.newListMap(); 
	
	@Override
	public TaskListenerManager add(TaskType type, TaskListener taskExecutor){
		this.mapper.putElement(type, taskExecutor);
		return this;
	}
	
	@Override
	public List<TaskListener> getTaskListeners(TaskType type){
//		if(!mapper.containsKey(type))
//			throw new BaseException("no task listener found: " + type);
		return mapper.get(type);
	}

}
