package org.onetwo.app.task;

import java.util.Map;

import com.google.common.collect.Maps;

@SuppressWarnings("rawtypes")
public class SimpleTaskListenerRegistry {

	private Map<TaskType, TaskListenerGroup> listenerMap;
	
	
	public SimpleTaskListenerRegistry() {
		super();
		registedDefaultListeners();
	}

	private void registedDefaultListeners(){
		Map<TaskType, TaskListenerGroup> listenerMap = Maps.newHashMap();
		prepareListener(listenerMap, type, listener);
	}
	
	private <T> void prepareListener(Map<TaskType, TaskListenerGroup> listenerMap, TaskType<T> type, TaskListener listener){
		TaskListenerGroup<T> group = new TaskListenerGroup<T>(type);
		group.addListener(listener);
		listenerMap.put(type, group);
		this.listenerMap = listenerMap;
	}
	
	public <T> TaskListenerGroup<T> getTaskListenerGroup(TaskType<T> type){
		TaskListenerGroup<T> group = this.listenerMap.get(type);
		return group;
	}

	public <T> SimpleTaskListenerRegistry registed(TaskType<T> type, TaskListener...listeners){
		TaskListenerGroup<T> group = getTaskListenerGroup(type);
		group.addListeners(listeners);
		return this;
	}
}
