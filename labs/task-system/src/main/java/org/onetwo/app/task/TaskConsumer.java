package org.onetwo.app.task;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

public class TaskConsumer {
	private static final Logger logger = MyLoggerFactory.getLogger(TaskConsumer.class);

	private TaskQueue taskQueue;
	private TaskListenerRegistry taskListenerRegistry;
	

	public TaskConsumer(TaskQueue taskQueue, TaskListenerRegistry taskListenerRegistry) {
		super();
		this.taskQueue = taskQueue;
		this.taskListenerRegistry = taskListenerRegistry;
	}

	public void doTask() throws InterruptedException {
		TaskData task = null;
		for(;;){
			task = taskQueue.take();
			logger.info("task consumer thread[{}] take task: {}", Thread.currentThread().getId(), task.getName());
			doProcess(task);
		}
	}
	
	protected void doProcess(TaskData task){
		List<TaskListener> listeners = this.taskListenerRegistry.getTaskListenerGroup(task.getType()).getListeners();
		for(TaskListener taskListener : listeners){
			logger.info("task consumer execute task listenr[{}] for task {}", taskListener.getClass(), task.getName());
			Object result = taskListener.onExecute(task);
			taskListener.afterExecute(task, result);
		}
	}

}
