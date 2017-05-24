package org.onetwo.common.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

/*****
 * ConcurrentRunnable.create(parties, ()->{
			//run task
		})
		.start()
		.await();
 * @author way
 *
 */
public class ConcurrentRunnable {
	public static ConcurrentRunnable create(int repeate, Runnable runnable){
		return new ConcurrentRunnable().concurrentRun(repeate, runnable);
	}
	
	/****
	 * 主线程等待一组线程里的每个线程countDown
	 */
	private CountDownLatch latch;
	/***
	 * 并通过CyclicBarrier把所有的线程阻塞到任务执行之前，直到所有线程都准备好
	 * 线程互相等待
	 */
	private CyclicBarrier barrier;
	private volatile boolean started;
	
	private List<Runnable> runnables = Lists.newArrayList();
	
	private ConcurrentRunnable() {
		super();
	}
	public ConcurrentRunnable concurrentRun(int concurrentSize, Runnable runnable){
		for (int i = 0; i < concurrentSize; i++) {
			this.runnables.add(runnable);
		}
		return this;
	}
	public ConcurrentRunnable addRunnables(Runnable... runnables){
		Stream.of(runnables).forEach(r->this.runnables.add(r));
		return this;
	}
	
	/****
	 * 启动所有线程
	 * @author wayshall
	 * @return
	 */
	public ConcurrentRunnable start(){
		int size = runnables.size();
		latch = new CountDownLatch(size);
		barrier = new CyclicBarrier(size);
		
		runnables.stream().forEach(r->{
			new Thread(()->{
				try {
					barrier.await();
				} catch (Exception e) {
					throw new RuntimeException("barrier await error!", e);
				}
				r.run();
				latch.countDown();
			}).start();
		});
		started = true;
		
		return this;
	}
	
	/****
	 * 等待所有线程任务执行完
	 * @author wayshall
	 */
	public void await(){
		if(!started)
			throw new RuntimeException("ConcurrentRunnable has not started!");
		try {
			this.latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException("CountDownLatch await error!", e);
		}
	}
}
