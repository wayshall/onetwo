package org.onetwo.app.task;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

public class TaskProduer {

	private static final Logger logger = MyLoggerFactory.getLogger(TaskProduer.class);

	private TaskQueue taskQueue;
	private TaskListenerRegistry taskListenerRegistry;
	
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
}
