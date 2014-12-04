package org.onetwo.plugins.akka.task.core;

import java.io.Serializable;

public class TaskResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3523016911350518264L;
	private final Object result;
	private final TaskMessage taskMessage;
	private final Throwable throwable;

	public TaskResult(Object data, TaskMessage task) {
		super();
		this.result = data;
		this.taskMessage = task;
		this.throwable = null;
	}
	
	public TaskResult(TaskMessage task, Throwable throwable) {
		super();
		this.taskMessage = task;
		this.throwable = throwable;
		this.result = null;
	}


	public Object getResult() {
		return result;
	}

	public TaskMessage getTaskMessage() {
		return taskMessage;
	}

	public Throwable getThrowable() {
		return throwable;
	}

}
