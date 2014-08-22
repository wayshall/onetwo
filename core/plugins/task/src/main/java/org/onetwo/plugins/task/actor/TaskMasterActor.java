package org.onetwo.plugins.task.actor;

import org.onetwo.plugins.task.service.TaskListenerManager;
import org.onetwo.plugins.task.utils.TaskData;
import org.onetwo.plugins.task.utils.TaskResult;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;

public class TaskMasterActor extends UntypedActor {
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private final ActorRef taskWorkActor;
	private final ActorRef taskResultActor;

	public TaskMasterActor(TaskListenerManager taskListenerManager, int numbOfWorkerInst) {
		super();
		taskWorkActor = getContext().actorOf(Props.create(TaskExecActor.class, taskListenerManager).withRouter(new RoundRobinPool(numbOfWorkerInst)));
		taskResultActor = getContext().actorOf(Props.create(TaskResultActor.class, taskListenerManager));
	}



	@Override
	public void onReceive(Object message) throws Exception {
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("TaskMasterActor receive message:\n");
		if(message instanceof TaskData){
			TaskData task = (TaskData) message;
			logMsg.append("task : ").append(task.getName()).append("\n");
			this.taskWorkActor.tell(task, getSelf());
		} else if(message instanceof TaskResult){
			logMsg.append("taskResult : ").append(message).append("\n");
			this.taskResultActor.tell(message, getSelf());
		}else{
			logMsg.append("unknown message : ").append(message);
		}
		logger.info(logMsg.toString());
	}

}
