package org.onetwo.app.task;

public class TaskListenerAdapter<T extends TaskData> implements TaskListener<T> {

	@Override
	public void onQueued(T task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object onExecute(T task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterExecute(T task, Object result) {
		// TODO Auto-generated method stub
		
	}

}
