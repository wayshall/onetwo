package org.onetwo.app.task;

public interface TaskListener<T extends TaskData> {
	
	void onQueued(T task);
	
	Object onExecute(T task);
	
	void afterExecute(T task, Object result);

}
