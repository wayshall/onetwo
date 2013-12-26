package org.onetwo.common.excel;

public class ExecutorModel {

	private String name;
	private String executor;

	private FieldValueExecutor fieldValueExecutor;

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

	public FieldValueExecutor getFieldValueExecutor() {
		return fieldValueExecutor;
	}

	public void setFieldValueExecutor(FieldValueExecutor iteratorRowValueExecutor) {
		this.fieldValueExecutor = iteratorRowValueExecutor;
	}

}
