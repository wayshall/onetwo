package org.onetwo.common.spring.concurrent;

import java.util.List;

public interface ParallelTaskExecutor<T> {
	
	public List<T> loadTaskList(int semaphoreCount);
	
	public void execute(T task);
	
	public void releaseSemaphore(T task);

}
