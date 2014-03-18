package org.onetwo.app.task;

public interface TaskListenerRegistry {

	public <T> TaskListenerGroup<T> getTaskListenerGroup(TaskType<T> type);

	public <T> TaskListenerRegistry registed(TaskType<T> type, TaskListener... listeners);

}