package org.onetwo.app.task;

import java.util.concurrent.BlockingQueue;

import com.google.common.collect.Queues;

public class SimpleTaskQueue {

	private BlockingQueue<TaskData> taskQueue = Queues.newArrayBlockingQueue(10);
	
	public boolean addTask(TaskData taskData){
		if(taskData==null)
			return false;
		return this.taskQueue.offer(taskData);
	}
	
	public TaskData take() throws InterruptedException{
		return this.taskQueue.take();
	}
}
