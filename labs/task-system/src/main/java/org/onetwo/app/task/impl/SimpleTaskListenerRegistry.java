package org.onetwo.app.task.impl;

import java.util.Map;

import org.onetwo.app.task.TaskListener;
import org.onetwo.app.task.TaskListenerGroup;
import org.onetwo.app.task.TaskListenerRegistry;
import org.onetwo.app.task.TaskType;

@SuppressWarnings("rawtypes")
public class SimpleTaskListenerRegistry implements TaskListenerRegistry {

	private Map<TaskType, TaskListenerGroup> listenerMap;
	
	
	public SimpleTaskListenerRegistry() {
		super();
		registedDefaultListeners();
	}

	private void registedDefaultListeners(){
//		Map<TaskType, TaskListenerGroup> listenerMap = Maps.newHashMap();
//		prepareListener(listenerMap, type, listener);
	}
	
	public static <T> void prepareListener(Map<TaskType, TaskListenerGroup> listenerMap, TaskType<T> type, TaskListener listener){
		TaskListenerGroup<T> group = new TaskListenerGroup<T>(type);
		group.addListener(listener);
		listenerMap.put(type, group);
	}
	
	public <T> TaskListenerGroup<T> getTaskListenerGroup(TaskType<T> type){
		TaskListenerGroup<T> group = this.listenerMap.get(type);
		if(group==null){
			group = new TaskListenerGroup<T>(type);
			this.listenerMap.put(type, group);
		}
		return group;
	}

	public <T> TaskListenerRegistry registed(TaskType<T> type, TaskListener...listeners){
		TaskListenerGroup<T> group = getTaskListenerGroup(type);
		group.addListeners(listeners);
		return this;
	}
}
