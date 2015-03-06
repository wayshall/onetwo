package org.onetwo.app.taskservice.service.impl;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.onetwo.app.taskservice.TaskServerConfig;
import org.onetwo.app.taskservice.actor.TaskMasterActor;
import org.onetwo.app.taskservice.service.TaskListenerManager;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Component
public class DefaultTaskProcessor implements InitializingBean, DisposableBean {

	private String systemName = "tasksys";
	private ActorRef masterAcotr;
	private ActorSystem system;
//	private int timeoutInSeconds=10;
	@Resource
	private TaskListenerManager taskListenerManager;
	@Resource
	private TaskQueueServiceImpl taskQueueService;
	
	private int numberOfWorkerInst = 1;
	private AtomicInteger queueSize = new AtomicInteger(0);
	
	@Resource
	private TaskServerConfig taskServerConfig;
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		TaskListenerManager taskListenerManager = SpringApplication.getInstance().getBean(TaskListenerManager.class, true);
		
		system = ActorSystem.create(systemName);
		masterAcotr = system.actorOf(Props.create(TaskMasterActor.class, taskListenerManager, taskQueueService, numberOfWorkerInst));
	}
	
	public void sendTask(TaskQueue taskData){
		masterAcotr.tell(taskData, ActorRef.noSender());
		queueSize.addAndGet(1);
	}
	
	public boolean isFull(){
		return taskServerConfig.isLimitedQueue()?(getQueueSize()==taskServerConfig.getQueueMaxSize()):false;
	}
	
	public int getQueueSize(){
		return this.queueSize.intValue();
	}

	public void setNumberOfWorkerInst(int numberOfWorkerInst) {
		this.numberOfWorkerInst = numberOfWorkerInst;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@Override
	public void destroy() throws Exception {
		this.system.shutdown();
	}

}
