package org.onetwo.plugins.akka.task.core;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.onetwo.plugins.akka.task.AkkaTaskConfig;
import org.onetwo.plugins.akka.task.actor.TaskMasterActor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class DefaultTaskProcessor implements InitializingBean, DisposableBean, TaskProcessor {

	private String systemName = "tasksys";
	private ActorRef masterAcotr;
	private ActorSystem system;
//	private int timeoutInSeconds=10;
	@Resource
	private TaskListenerManager taskListenerManager;
	private int numberOfWorkerInst = 1;
	private AtomicInteger queueSize = new AtomicInteger(0);
	
	@Resource
	private AkkaTaskConfig taskServerConfig;
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		TaskListenerManager taskListenerManager = SpringApplication.getInstance().getBean(TaskListenerManager.class, true);
		
		system = ActorSystem.create(systemName);
		masterAcotr = system.actorOf(Props.create(TaskMasterActor.class, taskListenerManager, numberOfWorkerInst));
	}
	
	@Override
	public void sendTask(TaskMessage taskData){
		masterAcotr.tell(taskData, ActorRef.noSender());
		queueSize.addAndGet(1);
	}
	
	@Override
	public boolean isFull(){
		return taskServerConfig.isLimitedQueue()?(getQueueSize()==taskServerConfig.getQueueMaxSize()):false;
	}
	
	@Override
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
