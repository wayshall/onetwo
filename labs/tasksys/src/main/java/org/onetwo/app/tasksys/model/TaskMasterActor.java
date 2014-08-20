package org.onetwo.app.tasksys.model;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TaskMasterActor extends UntypedActor {
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private final ActorRef taskWorkActor;
	private final ActorRef taskResultActor;

	public TaskMasterActor(ActorRef taskWorkActor, ActorRef taskResultActor) {
		super();
		this.taskWorkActor = taskWorkActor;
		this.taskResultActor = taskResultActor;
	}



	@Override
	public void onReceive(Object message) throws Exception {
//		TaskListenerManager taskListenerManager = SpringApplication.getInstance().getBean(TaskListenerManager.class, true);
		
		if(message instanceof TaskData){
			TaskData task = (TaskData) message;
			logger.info("receive task: " + task.getName());
			this.taskWorkActor.tell(task, getSelf());
		} else if(message instanceof TaskResult){
			this.taskResultActor.tell(message, getSelf());
		}else{
			logger.info("unknown task:" + message);
		}
	}

}
