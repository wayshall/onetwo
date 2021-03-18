package org.onetwo.common.spring.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */

abstract public class BaseExccutorService implements InitializingBean, DisposableBean {
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());

	final private String threadName;
	private ExecutorService executor;
	private int threadNumber = 1;
	private AtomicBoolean stop = new AtomicBoolean(false);

	public BaseExccutorService(String threadName) {
		Assert.hasText(threadName, "threadName must has text");
		this.threadName = threadName;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
        executor = Executors.newFixedThreadPool(threadNumber, new ThreadFactory() {
    		@Override
    		public Thread newThread(Runnable r) {
    			return new Thread(r, threadName);
    		}
    	});
        start();
	}

	
	public void stop() {
		this.stop.set(true);
	}
	
	public boolean isStop() {
		return this.stop.get();
	}
	
	public void start() {
        this.executor.execute(() -> {
        	doTask();
        });
	}

	abstract protected void doTask();

//	protected void doTask() {
//		try {
//			while(true) {
				// do something
//			}
//		} catch (Throwable e) {
//			logger.error("记录错误", e);
//		}
//		// 若非停止状态，重新开始...
//		if (!isStop()) {
//			logger.info("非正常关闭，尝试重新……");
//			this.doTask();
//		}
//	}
	
	public void shutdownExecutor() {
		if (executor!=null) {
			executor.shutdown();
			executor = null;
		}
	}

	@Override
	public void destroy() throws Exception {
		this.shutdownExecutor();
	}

    protected void finalize() {
		this.shutdownExecutor();
    }
}
