package org.onetwo.app.task;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.list.JFishList;

public class TaskListenerGroup {
	
	private TaskType taskType;
	private List<TaskListener> listeners;
	
	public TaskListenerGroup(TaskType taskType) {
		super();
		this.taskType = taskType;
	}
	public TaskType getTaskType() {
		return taskType;
	}
	
	public int size(){
		return this.listeners==null?0:this.listeners.size();
	}
	
	public void clear(){
		if(this.listeners!=null)
			this.listeners.clear();
	}
	
	public void addListeners(TaskListener... listeners){
		for(TaskListener listener : listeners){
			addListener(listener);
		}
	}
	
	public void addListener(TaskListener listener){
		if(this.listeners==null){
			this.listeners = JFishList.create();
		}
		this.listeners.add(listener);
	}
	public List<TaskListener> getListeners() {
		return listeners==null?Collections.EMPTY_LIST:listeners;
	}
	
	
	

}
