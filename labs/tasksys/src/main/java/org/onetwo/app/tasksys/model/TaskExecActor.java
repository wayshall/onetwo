package org.onetwo.app.tasksys.model;

import java.util.List;

import org.onetwo.app.tasksys.model.service.TaskExecuteListener;
import org.onetwo.app.tasksys.model.service.TaskListenerManager;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.LangUtils;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TaskExecActor extends UntypedActor {
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private final TaskListenerManager taskListenerManager;
	
	
	public TaskExecActor(TaskListenerManager taskListenerManager) {
		super();
		this.taskListenerManager = taskListenerManager;
	}

	@Override
	public void onReceive(Object taskInfo) throws Exception {
		if(taskInfo instanceof TaskData){
			TaskData task = (TaskData) taskInfo;
			logger.info("execotr receive task: " + task.getName());
			LangUtils.await(3);
			logger.info("executed task : {}", task.getName());
			List<TaskExecuteListener<?>> listeners = taskListenerManager.getTaskExecuteListeners(task.getTaskType());
			if(LangUtils.isNotEmpty(listeners)){
				for(TaskExecuteListener<?> l : listeners){
					Object rs = l.execute(task);
					if(task.isReply()){
						getSender().tell(new TaskResult(rs, task), getSelf());
					}
				}
			}else{
				logger.info("no listener found for task type: " + task.getTaskType());
			}
			
		}else{
			logger.info("unknown task:" + taskInfo);
		}
	}

}
