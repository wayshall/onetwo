package org.onetwo.plugins.task.actor;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.task.service.TaskExecuteListener;
import org.onetwo.plugins.task.service.TaskListenerManager;
import org.onetwo.plugins.task.utils.TaskData;
import org.onetwo.plugins.task.utils.TaskResult;

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
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("TaskExecActor receive :\n");
		if(taskInfo instanceof TaskData){
			TaskData task = (TaskData) taskInfo;
			logMsg.append("task : ").append(task.getName()).append("\n");
			List<TaskExecuteListener> listeners = taskListenerManager.getTaskExecuteListeners(task.getTaskType());
			if(LangUtils.isNotEmpty(listeners)){
				for(TaskExecuteListener l : listeners){
					Object rs = l.execute(task);
					if(task.isReply()){
						getSender().tell(new TaskResult(rs, task), getSelf());
					}
				}
			}else{
				logMsg.append("no listener found for task type: " + task.getTaskType());
			}
			
		}else{
			logMsg.append("unknown task : ").append(taskInfo);
		}
		logger.info(logMsg.toString());
	}

}
