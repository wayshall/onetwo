package org.onetwo.app.tasksys.model;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TaskActor extends UntypedActor {
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object taskInfo) throws Exception {
		if(taskInfo instanceof ReplyTaskData){
			ReplyTaskData task = (ReplyTaskData) taskInfo;
			logger.info("receive reply task: " + task.getName());
			task.setResult(new TaskResult());
			/*if(task.getCurrentTimes()%10==0)
				throw new BaseException("error");*/
			getSender().tell(taskInfo, getSelf());
		}else if(taskInfo instanceof TaskData){
			TaskData task = (TaskData) taskInfo;
			logger.info("receive task: " + task.getName());
			
		}else{
			logger.info("unknown task:" + taskInfo);
		}
	}

}
