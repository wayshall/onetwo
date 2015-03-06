package org.onetwo.common.spring.concurrent;

import java.util.List;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/***
 * 并发任务，必要时启用
 * @author way
 */
//@Component
public class ParallelTask implements InitializingBean {
	
	private final Logger logger = JFishLoggerFactory.logger(this.getClass());
	
	@Resource
	private ThreadPoolTaskScheduler taskScheduler;
	
	private int semaphoreCount = 10;
	private Semaphore semaphore;
	
	@Resource
	private ApplicationContext applicationContext;
	
	@Resource
	private List<ParallelTaskExecutor<Object>> parallelTaskExecutors;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.semaphore = new Semaphore(semaphoreCount);
	}

	public void execute() {
		logger.info("开始并行执行任务……");

		final int taskCount = semaphore.availablePermits();
		if(taskCount<1){
			logger.info("任务已满，忽略此次执行……");
			return ;
		}
		for(final ParallelTaskExecutor<Object> taskExe : this.parallelTaskExecutors){
			List<Object> tasklist = taskExe.loadTaskList(semaphoreCount);
			for(final Object task : tasklist){
				taskScheduler.submit(new Runnable() {
					
					@Override
					public void run() {
						try {
							semaphore.acquire();
//							LangUtils.await(20*s);
							logger.info("线程[{}] 开始执行任务[{}]……", Thread.currentThread().getId(), task);
							taskExe.execute(task);
						} catch (Exception e) {
							logger.error("执行任务["+task+"]发生错误："+e.getMessage(), e);
						}finally{
							semaphore.release();
							logger.info("任务[{}]执行结束……", task);
							taskExe.releaseSemaphore(task);
						}
					}
				});
			}
		}
		
	}
	
	
}
