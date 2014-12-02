package org.onetwo.app.taskservice.actor;

import java.util.List;

import org.onetwo.app.taskservice.service.TaskExecuteListener;
import org.onetwo.app.taskservice.service.TaskListenerManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.utils.TaskResult;
import org.slf4j.Logger;

import akka.actor.UntypedActor;

public class TaskExecActor extends UntypedActor {
	
//	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private final TaskListenerManager taskListenerManager;
	
	
	public TaskExecActor(TaskListenerManager taskListenerManager) {
		super();
		this.taskListenerManager = taskListenerManager;
	}

	@Override
	public void onReceive(Object taskInfo) throws Exception {
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("TaskExecActor receive :\n");
		if(taskInfo instanceof TaskQueue){
			TaskQueue task = (TaskQueue) taskInfo;
			logMsg.append("task : ").append(task.getName()).append("\n");
			List<TaskExecuteListener> listeners = taskListenerManager.getTaskExecuteListeners(task.getTaskType());
			if(LangUtils.isNotEmpty(listeners)){
				for(TaskExecuteListener l : listeners){
					TaskResult taskResult = null;
					try {
						Object rs = l.execute(task);
						taskResult = new TaskResult(rs, task);
						logMsg.append("listener[").append(l.getClass().getName()).append("] execute succeed.");
						logger.info(logMsg.toString());
					} catch (Exception e) {
						taskResult = new TaskResult(task, e);
						logMsg.append("listener[").append(l.getClass().getName()).append("] execute error: ").append(e.getMessage());
						logger.info(logMsg.toString(), e);
					}
					if(task.isReply()){
						getSender().tell(taskResult, getSelf());
					}
				}
			}else{
				logMsg.append("no listener found for task type: " + task.getTaskType());
				logger.info(logMsg.toString());
			}
			
		}else{
			logMsg.append("unknown task : ").append(taskInfo);
			logger.info(logMsg.toString());
		}
	}

}
