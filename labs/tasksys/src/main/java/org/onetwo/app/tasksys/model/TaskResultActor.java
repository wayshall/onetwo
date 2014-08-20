package org.onetwo.app.tasksys.model;

import java.util.List;

import org.onetwo.app.tasksys.model.service.TaskCompleteListener;
import org.onetwo.app.tasksys.model.service.TaskListenerManager;
import org.onetwo.common.utils.LangUtils;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TaskResultActor extends UntypedActor {
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);


	private final TaskListenerManager taskListenerManager;
	
	public TaskResultActor(TaskListenerManager taskListenerManager) {
		super();
		this.taskListenerManager = taskListenerManager;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof TaskResult){
			TaskResult result = (TaskResult) message;
			logger.info("receive TaskResult: " + result);
			List<TaskCompleteListener> listeners = taskListenerManager.getTaskCompleteListeners(result.getTask().getTaskType());
			if(LangUtils.isNotEmpty(listeners)){
				for(TaskCompleteListener l : listeners){
					l.onComplete(result);
				}
			}else{
				logger.info("no TaskCompleteListener found for task type: " + result.getTask().getTaskType());
			}
			
		}else{
			logger.info("unknown message:" + message);
		}
	}

}
