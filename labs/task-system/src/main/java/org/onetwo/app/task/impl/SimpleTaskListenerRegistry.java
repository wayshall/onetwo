package org.onetwo.app.task.impl;

import java.util.Map;

import org.onetwo.app.task.TaskListener;
import org.onetwo.app.task.TaskListenerGroup;
import org.onetwo.app.task.TaskListenerRegistry;
import org.onetwo.app.task.TaskType;

import com.google.common.collect.Maps;

@SuppressWarnings("rawtypes")
public class SimpleTaskListenerRegistry implements TaskListenerRegistry {

	private Map<TaskType, TaskListenerGroup> listenerMap = Maps.newHashMap();
	
	
	public SimpleTaskListenerRegistry() {
		super();
		registedDefaultListeners();
	}

	private void registedDefaultListeners(){
//		Map<TaskType, TaskListenerGroup> listenerMap = Maps.newHashMap();
//		prepareListener(listenerMap, type, listener);
	}
	
	public static void prepareListener(Map<TaskType, TaskListenerGroup> listenerMap, TaskType type, TaskListener listener){
		TaskListenerGroup group = new TaskListenerGroup(type);
		group.addListener(listener);
		listenerMap.put(type, group);
	}
	
	public  TaskListenerGroup getTaskListenerGroup(TaskType type){
		TaskListenerGroup group = this.listenerMap.get(type);
		if(group==null){
			group = new TaskListenerGroup(type);
			this.listenerMap.put(type, group);
		}
		return group;
	}

	public  TaskListenerRegistry registed(TaskType type, TaskListener...listeners){
		TaskListenerGroup group = getTaskListenerGroup(type);
		group.addListeners(listeners);
		return this;
	}
}
