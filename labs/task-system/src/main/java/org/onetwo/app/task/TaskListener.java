package org.onetwo.app.task;

public interface TaskListener {
	
	void onQueued(TaskData task);
	
	Object onExecute(TaskData task);
	
	void afterExecute(TaskData task, Object result);

}
