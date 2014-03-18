package org.onetwo.app.task;

public interface TaskListener {
	
	void onOffered(TaskData task);
	
	Object onExecute(TaskData task);
	
	void afterExecute(TaskData task, Object result);

}
