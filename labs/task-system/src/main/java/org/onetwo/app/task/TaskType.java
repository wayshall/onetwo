package org.onetwo.app.task;

public class TaskType<T> {
	
	public static final TaskType<T>
	
	private final String name;
	private final Class<T> listenerType;
	
	public TaskType(String name, Class<T> listenerType) {
		super();
		this.name = name;
		this.listenerType = listenerType;
	}

	public String getName() {
		return name;
	}

	public Class<T> getListenerType() {
		return listenerType;
	}
	
}
