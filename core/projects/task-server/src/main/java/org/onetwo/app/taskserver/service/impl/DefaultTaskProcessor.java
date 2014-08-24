package org.onetwo.app.taskserver.service.impl;

import javax.annotation.Resource;

import org.onetwo.app.taskserver.actor.TaskMasterActor;
import org.onetwo.app.taskserver.service.TaskListenerManager;
import org.onetwo.plugins.task.utils.TaskData;
import org.springframework.beans.factory.InitializingBean;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class DefaultTaskProcessor implements InitializingBean {

	private String systemName = "tasksys";
	private ActorRef masterAcotr;
	private ActorSystem system;
//	private int timeoutInSeconds=10;
	@Resource
	private TaskListenerManager taskListenerManager;
	private int numberOfWorkerInst = 1;
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		TaskListenerManager taskListenerManager = SpringApplication.getInstance().getBean(TaskListenerManager.class, true);
		
		system = ActorSystem.create(systemName);
		masterAcotr = system.actorOf(Props.create(TaskMasterActor.class, taskListenerManager, numberOfWorkerInst));
	}
	
	public void sendTask(TaskData taskData){
		masterAcotr.tell(taskData, ActorRef.noSender());
	}

	public void setNumberOfWorkerInst(int numberOfWorkerInst) {
		this.numberOfWorkerInst = numberOfWorkerInst;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}
