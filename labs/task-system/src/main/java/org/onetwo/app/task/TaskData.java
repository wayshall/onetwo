package org.onetwo.app.task;

public class TaskData {

	private TaskType<?> type;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TaskType<?> getType() {
		return type;
	}
	public void setType(TaskType<?> type) {
		this.type = type;
	}
	
}
