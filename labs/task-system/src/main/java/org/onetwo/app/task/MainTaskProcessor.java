package org.onetwo.app.task;

import java.util.List;


public class MainTaskProcessor {
	
	private SimpleTaskQueue taskQueue;
	private SimpleTaskListenerRegistry taskListenerRegistry;
	
	public MainTaskProcessor(SimpleTaskQueue taskQueue) {
		super();
		this.taskQueue = taskQueue;
	}

	public void doTask() throws InterruptedException{
		TaskData task = null;
		for(;;){
			task = taskQueue.take();
			if(task==null)
				continue;
			doProcess(task);
		}
	}
	
	protected void doProcess(TaskData task){
		List<TaskListener> listeners = this.taskListenerRegistry.getTaskListenerGroup(task.getType()).getListeners();
		for(TaskListener taskListener : listeners){
			Object result = taskListener.onExecute(task);
			taskListener.afterExecute(task, result);
		}
	}

}
