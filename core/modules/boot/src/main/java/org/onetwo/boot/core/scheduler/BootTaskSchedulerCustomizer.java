package org.onetwo.boot.core.scheduler;

import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/***
 * 相当于
 spring: 
 	 task: 
        scheduling: 
            pool: 
                size: 3
 * @author way
 *
 */
public class BootTaskSchedulerCustomizer implements TaskSchedulerCustomizer {
	
	@Override
	public void customize(ThreadPoolTaskScheduler taskScheduler) {
		if (taskScheduler==null) {
			return ;
		}
		// 强制设置多个线程，避免默认单个线程，而多个@schedule任务时，导致无法并发执行任务
		if (taskScheduler.getPoolSize()==1) {
			taskScheduler.setPoolSize(3);
		}
	}

}
