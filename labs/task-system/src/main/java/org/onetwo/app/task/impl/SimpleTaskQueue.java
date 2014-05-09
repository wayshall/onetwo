package org.onetwo.app.task.impl;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.onetwo.app.task.TaskData;
import org.onetwo.app.task.TaskQueue;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.DateUtil;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import com.google.common.collect.Queues;

public class SimpleTaskQueue implements TaskQueue {

	private static final Logger logger = MyLoggerFactory.getLogger(SimpleTaskQueue.class);
	
	private BlockingQueue<TaskData> taskQueue;
//	private final SimpleTaskListenerRegistry taskListenerRegistry;
	
//	private TaskListenerRegistry taskListenerRegistry;
	
	public SimpleTaskQueue(int capacity) {
		super();
		taskQueue = Queues.newArrayBlockingQueue(capacity);
//		this.taskListenerRegistry = taskListenerRegistry;
	}

	public boolean offerTask(TaskData taskData){
		if(taskData==null)
			return false;
		if(this.taskQueue.offer(taskData)){
			logger.info(">>> thread[{}] offer task[{}] to queue .", Thread.currentThread().getId(), taskData.getName());
			this.executeOnQueued(taskData);
			return true;
		}else{
			return false;
		}
	}

	public void offerTask(TaskData taskData, long seconds){
		Assert.notNull(taskData);
		try {
			if(this.taskQueue.offer(taskData, seconds, TimeUnit.SECONDS)){
				logger.info(">>> thread[{}] offer task[{}] to queue .", Thread.currentThread().getId(), taskData.getName());
				this.executeOnQueued(taskData);
			}
		} catch (InterruptedException e) {
			logger.error(">>> thread[{"+Thread.currentThread().getId()+"}] is interrupted: " + taskData.getName(), e);
			Thread.currentThread().interrupt();
			return ;
		}
	}
	
	public void putTask(TaskData taskData){
		try {
			String ts1 = DateUtil.formatDateTimeMillis(new Date());
			
			this.taskQueue.put(taskData);
			
			String ts2 = DateUtil.formatDateTimeMillis(new Date());
			logger.info(">>> thread[{}] prepare to put task[{}] to queue at "+ts1+", and has putted to queue "+ts2+" .", Thread.currentThread().getId(), taskData.getName());
		} catch (InterruptedException e) {
			logger.error(">>> thread[{"+Thread.currentThread().getId()+"}] is interrupted: " + taskData.getName(), e);
			Thread.currentThread().interrupt();
			return ;
		}
		this.executeOnQueued(taskData);
	}
	
	private void executeOnQueued(TaskData taskData){
		/*List<TaskListener> listeners = taskListenerRegistry.getTaskListenerGroup(taskData.getType()).getListeners();
		for(TaskListener taskListener : listeners){
			try {
				taskListener.onQueued(taskData);
			} catch (Exception e) {
				logger.error(">>> thread[{"+Thread.currentThread().getId()+"}] TaskListener["+taskListener+"] of task["+taskData.getName()+"] onQueued error: " + e.getMessage(), e);
			}
		}*/
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
