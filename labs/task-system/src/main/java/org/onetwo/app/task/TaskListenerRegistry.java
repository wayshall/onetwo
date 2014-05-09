package org.onetwo.app.task;

public interface TaskListenerRegistry {

	public  TaskListenerGroup getTaskListenerGroup(TaskType type);

	public  TaskListenerRegistry registed(TaskType type, TaskListener... listeners);

}