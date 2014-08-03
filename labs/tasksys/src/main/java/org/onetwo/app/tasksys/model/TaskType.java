package org.onetwo.app.tasksys.model;


public class TaskType {

//	public static final Map<String, TaskType> tasks = LangUtils.newHashMap();
	

	private final String code;
	private final String name;
	
	public TaskType(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

}
