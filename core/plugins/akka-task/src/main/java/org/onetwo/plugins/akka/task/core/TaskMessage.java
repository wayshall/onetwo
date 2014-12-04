package org.onetwo.plugins.akka.task.core;


public interface TaskMessage {
	public TaskType getTaskType();
	public boolean isReply();
}
