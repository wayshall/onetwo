package org.onetwo.app.tasksys.model.service.impl;

import javax.annotation.Resource;

import org.onetwo.app.tasksys.model.TaskData;
import org.onetwo.app.tasksys.model.TaskMasterActor;
import org.onetwo.app.tasksys.model.service.TaskListenerManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Component
public class TaskProcessor implements InitializingBean {

	private String actorName = "tasksys";
	private ActorRef masterAcotr;
	private ActorSystem system;
//	private int timeoutInSeconds=10;
	@Resource
	private TaskListenerManager taskListenerManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		TaskListenerManager taskListenerManager = SpringApplication.getInstance().getBean(TaskListenerManager.class, true);
		
		system = ActorSystem.create(actorName);
		masterAcotr = system.actorOf(Props.create(TaskMasterActor.class, taskListenerManager));
	}
	
	public void sendTask(TaskData taskData){
		masterAcotr.tell(taskData, ActorRef.noSender());
	}

}
