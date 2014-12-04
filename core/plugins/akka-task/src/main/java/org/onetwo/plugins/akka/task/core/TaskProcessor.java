package org.onetwo.plugins.akka.task.core;

public interface TaskProcessor {

	public void sendTask(TaskMessage taskData);

	public boolean isFull();

	public int getQueueSize();

}