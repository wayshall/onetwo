package org.onetwo.app.task.impl;

import java.util.concurrent.BlockingQueue;

import org.onetwo.app.task.TaskData;
import org.onetwo.app.task.TaskQueue;
import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

import com.google.common.collect.Queues;

public class SimpleTaskQueue implements TaskQueue {

	private static final Logger logger = MyLoggerFactory.getLogger(SimpleTaskQueue.class);
	
	private BlockingQueue<TaskData> taskQueue = Queues.newArrayBlockingQueue(10);
//	private final SimpleTaskListenerRegistry taskListenerRegistry;
	
	
	public SimpleTaskQueue() {
		super();
//		this.taskListenerRegistry = taskListenerRegistry;
	}
	
	
	public boolean offer(TaskData e) {
		return taskQueue.offer(e);
	}


	public TaskData take() throws InterruptedException {
		return this.taskQueue.take();
		/*try {
			return this.taskQueue.take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}*/
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
