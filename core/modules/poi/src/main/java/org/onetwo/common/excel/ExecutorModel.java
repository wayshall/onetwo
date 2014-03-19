package org.onetwo.common.excel;

public class ExecutorModel {

	private String name;
	private String executor;
	private FieldValueExecutor instance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public FieldValueExecutor getInstance() {
		return instance;
	}

	public void setInstance(FieldValueExecutor instance) {
		this.instance = instance;
	}

}
