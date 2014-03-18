package org.onetwo.app.task;


public interface TaskQueue {

	public boolean offer(TaskData e);

	public TaskData take() throws InterruptedException;

	public int size();

	public boolean isEmpty();

	public void clear();

}