package org.onetwo.app.task;

public interface TaskListener<T extends TaskData> {
	
//	void onQueued(T task);
	
	Object onExecute(T task);
	
	/****
	 * 发生异常时回调
	 * 如果再抛出异常，则不再处理
	 * @param task
	 * @param e
	 */
	void onException(T task, Exception e);
	
//	void afterExecute(T task, Object result);

}
