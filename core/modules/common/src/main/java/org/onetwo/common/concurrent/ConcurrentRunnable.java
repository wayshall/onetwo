package org.onetwo.common.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public class ConcurrentRunnable {
	private CountDownLatch latch;
	private CyclicBarrier barrier;
	private volatile boolean started;
	
	private List<Runnable> runnables = Lists.newArrayList();
	
	public ConcurrentRunnable() {
		super();
	}
	public ConcurrentRunnable repeate(int repeate, Runnable runnable){
		for (int i = 0; i < repeate; i++) {
			this.runnables.add(runnable);
		}
		return this;
	}
	public ConcurrentRunnable addRunnables(Runnable... runnables){
		Stream.of(runnables).forEach(r->this.runnables.add(r));
		return this;
	}
	
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
