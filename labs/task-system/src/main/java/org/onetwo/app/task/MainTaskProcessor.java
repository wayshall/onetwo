package org.onetwo.app.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;


public class MainTaskProcessor {
	private static final Logger logger = MyLoggerFactory.getLogger(MainTaskProcessor.class);
	private final ExecutorService executor;
	private int executorCount;
	private TaskQueue taskQueue;
	private TaskListenerRegistry taskListenerRegistry;
	
	
	public MainTaskProcessor() {
		super();
		this.executor = Executors.newCachedThreadPool();
	}

	public MainTaskProcessor(int executorCount, TaskQueue taskQueue, TaskListenerRegistry taskListenerRegistry) {
		this();
		this.executorCount = executorCount;
		this.taskQueue = taskQueue;
		this.taskListenerRegistry = taskListenerRegistry;
	}

	public void doProcess(){
//		final TaskQueue taskQueue = SpringApplication.getInstance().getBean(TaskQueue.class);
//		final TaskListenerRegistry taskListenerRegistry = SpringApplication.getInstance().getBean(TaskListenerRegistry.class);
		final TaskConsumer taskConsumer = new TaskConsumer(taskQueue, taskListenerRegistry);
		for(int i=0; i<executorCount; i++){
			executor.submit(new Runnable() {
				
				public void run() {
					try {
						taskConsumer.doTask();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						logger.info("thread[{}] is interrupt.", Thread.currentThread().getId());
					}
				}
			});
		}
	}
	

}
