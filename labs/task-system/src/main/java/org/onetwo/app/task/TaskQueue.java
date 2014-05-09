package org.onetwo.app.task;


public interface TaskQueue {

	public boolean offerTask(TaskData taskData);
	
	public void putTask(TaskData e) throws InterruptedException;

	public TaskData take() throws InterruptedException;

	public int size();

	public boolean isEmpty();

	public void clear();

}