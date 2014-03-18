package org.onetwo.app.task;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.google.common.collect.Queues;

public class SimpleTaskQueue {

	private BlockingQueue<TaskData> taskQueue = Queues.newArrayBlockingQueue(10);
	private final SimpleTaskListenerRegistry taskListenerRegistry;
	
	
	public SimpleTaskQueue(SimpleTaskListenerRegistry taskListenerRegistry) {
		super();
		this.taskListenerRegistry = taskListenerRegistry;
	}

	public boolean addTask(TaskData taskData){
		if(taskData==null)
			return false;
		if(this.taskQueue.offer(taskData)){
			List<TaskListener> listeners = taskListenerRegistry.getTaskListenerGroup(taskData.getType()).getListeners();
			for(TaskListener taskListener : listeners){
				taskListener.onOffered(taskData);
			}
			return true;
		}
		return false;
	}
	
	public TaskData take(){
		try {
			return this.taskQueue.take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return null;
	}

	public int size() {
		return taskQueue.size();
	}

	public boolean isEmpty() {
		return taskQueue.isEmpty();
	}

	public TaskData poll() {
		return taskQueue.poll();
	}

	public void clear() {
		taskQueue.clear();
	}
	
	
}
