package org.onetwo.app.task.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.onetwo.app.task.TaskData;
import org.onetwo.app.task.TaskListener;
import org.onetwo.app.task.TaskListenerRegistry;
import org.onetwo.app.task.TaskQueue;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.DateUtil;
import org.slf4j.Logger;

import com.google.common.collect.Queues;

public class SimpleTaskQueue implements TaskQueue {

	private static final Logger logger = MyLoggerFactory.getLogger(SimpleTaskQueue.class);
	
	private BlockingQueue<TaskData> taskQueue = Queues.newArrayBlockingQueue(10);
//	private final SimpleTaskListenerRegistry taskListenerRegistry;
	
	private TaskListenerRegistry taskListenerRegistry;
	
	public SimpleTaskQueue(TaskListenerRegistry taskListenerRegistry) {
		super();
		this.taskListenerRegistry = taskListenerRegistry;
	}

	public boolean offerTask(TaskData taskData){
		if(taskData==null)
			return false;
		if(this.taskQueue.offer(taskData)){
			logger.info(">>> thread[{}] offer task[{}] to queue .", Thread.currentThread().getId(), taskData.getName());
			List<TaskListener> listeners = taskListenerRegistry.getTaskListenerGroup(taskData.getType()).getListeners();
			for(TaskListener taskListener : listeners){
				taskListener.onQueued(taskData);
			}
			return true;
		}
		return false;
	}
	
	public void putTask(TaskData taskData){
		try {
			String ts1 = DateUtil.formatDateTimeMillis(new Date());
			this.taskQueue.put(taskData);
			String ts2 = DateUtil.formatDateTimeMillis(new Date());
			logger.info(">>> thread[{}] prepare to put task[{}] to queue at "+ts1+", and has putted task[{}] to queue "+ts2+" .", Thread.currentThread().getId(), taskData.getName());
		} catch (InterruptedException e) {
			logger.error(">>> thread is interrupted: " + taskData.getName(), e);
			Thread.currentThread().interrupt();
			return ;
		}
		List<TaskListener> listeners = taskListenerRegistry.getTaskListenerGroup(taskData.getType()).getListeners();
		for(TaskListener taskListener : listeners){
			taskListener.onQueued(taskData);
		}
	}
	

	@Override
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
