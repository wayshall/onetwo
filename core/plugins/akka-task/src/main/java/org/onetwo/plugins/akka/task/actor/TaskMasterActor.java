package org.onetwo.plugins.akka.task.actor;

import org.onetwo.plugins.akka.task.core.TaskListenerManager;
import org.onetwo.plugins.akka.task.core.TaskMessage;
import org.onetwo.plugins.akka.task.core.TaskResult;

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
		if(message instanceof TaskMessage){
			TaskMessage task = (TaskMessage) message;
			logMsg.append("taskMessage : ").append(task).append("\n");
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
