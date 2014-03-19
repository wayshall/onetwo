package org.onetwo.app.task;

import javax.annotation.PostConstruct;

import org.onetwo.app.task.impl.SimpleTaskListenerRegistry;
import org.onetwo.app.task.impl.SimpleTaskQueue;
import org.onetwo.app.task.job.DataSyncTask;
import org.onetwo.app.task.test.EmailListener;
import org.onetwo.app.task.test.TaskTypeConstant;
import org.onetwo.common.spring.config.JFishProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@JFishProfile
@ImportResource("classpath:applicationContext.xml")
public class TaskContextConfig {
	
	/*@Resource
	private TaskListenerRegistry taskListenerRegistry;*/
	
	@Bean
	public TaskConfig taskConfig(){
		return TaskConfig.getInstance();
	}
	
	@Bean
	public TaskQueue taskQueue(){
		TaskQueue taskQueue = new SimpleTaskQueue(taskListenerRegistry());
		return taskQueue;
	}
	
	@Bean
	public TaskListenerRegistry taskListenerRegistry(){
		TaskListenerRegistry reg = new SimpleTaskListenerRegistry();
		return reg;
	}

	@Bean
	public MainTaskProcessor mainTaskProcessor(){
		MainTaskProcessor tp = new MainTaskProcessor(taskConfig().getTaskConsumerCount(), taskQueue(), taskListenerRegistry());
		return tp;
	}
	
	/*@Bean
	public TaskProduer taskProduer(){
		TaskProduer taskProduer = new TaskProduer();
		return taskProduer;
	}*/
	
	@Bean
	public DataSyncTask dataSyncTask(){
		DataSyncTask dataSyncTask = new DataSyncTask();
		return dataSyncTask;
	}
	
	@PostConstruct
	public void init(){
		TaskListenerRegistry reg = taskListenerRegistry();
		reg.registed(TaskTypeConstant.EMAIL, new EmailListener());
	}
}
