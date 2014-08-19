package org.onetwo.app.tasksys.model;

import java.util.List;

import org.onetwo.app.tasksys.model.service.TaskListener;
import org.onetwo.app.tasksys.model.service.TaskListenerManager;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TaskActor extends UntypedActor {
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private TaskListenerManager taskListenerManager = SpringApplication.getInstance().getBean(TaskListenerManager.class);
	
	@Override
	public void onReceive(Object taskInfo) throws Exception {
		if(taskInfo instanceof ReplyTaskData){
			ReplyTaskData task = (ReplyTaskData) taskInfo;
			logger.info("receive reply task: " + task.getName());
			
			List<TaskListener> listeners = taskListenerManager.getTaskListeners(task.getTaskType());
			if(LangUtils.isNotEmpty(listeners)){
				JFishList<TaskResult> rslist = JFishList.create();
				for(TaskListener l : listeners){
					TaskResult tr = l.execute(task);
					rslist.add(tr);
				}
				getSender().tell(rslist, getSelf());
			}else{
				logger.info("no listener found for task type: " + task.getTaskType());
			}
			
			
		}else if(taskInfo instanceof TaskData){
			TaskData task = (TaskData) taskInfo;
			logger.info("receive task: " + task.getName());
			List<TaskListener> listeners = taskListenerManager.getTaskListeners(task.getTaskType());
			if(LangUtils.isNotEmpty(listeners)){
				for(TaskListener l : listeners){
					l.execute(task);
				}
			}else{
				logger.info("no listener found for task type: " + task.getTaskType());
			}
			
		}else{
			logger.info("unknown task:" + taskInfo);
		}
	}

	public TaskListenerManager getTaskListenerManager() {
		return taskListenerManager;
	}

	public void setTaskListenerManager(TaskListenerManager taskListenerManager) {
		this.taskListenerManager = taskListenerManager;
	}

}
