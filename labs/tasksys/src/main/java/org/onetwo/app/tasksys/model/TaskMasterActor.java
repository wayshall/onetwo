package org.onetwo.app.tasksys.model;

import org.onetwo.app.tasksys.model.service.TaskListenerManager;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;

public class TaskMasterActor extends UntypedActor {
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private final ActorRef taskWorkActor;
	private final ActorRef taskResultActor;

	public TaskMasterActor(TaskListenerManager taskListenerManager) {
		super();
		taskWorkActor = getContext().actorOf(Props.create(TaskExecActor.class, taskListenerManager).withRouter(new RoundRobinRouter(2)));
		taskResultActor = getContext().actorOf(Props.create(TaskResultActor.class, taskListenerManager));
	}



	@Override
	public void onReceive(Object message) throws Exception {
//		TaskListenerManager taskListenerManager = SpringApplication.getInstance().getBean(TaskListenerManager.class, true);
		
		if(message instanceof TaskData){
			TaskData task = (TaskData) message;
			logger.info("master receive task: " + task.getName());
			this.taskWorkActor.tell(task, getSelf());
		} else if(message instanceof TaskResult){
			this.taskResultActor.tell(message, getSelf());
		}else{
			logger.info("unknown task:" + message);
		}
	}

}
