package org.onetwo.common.db.generator;

import java.util.List;

public class GeneratedResult<T> {
	private String tableName;
	private List<T> generatedList;
	
	public GeneratedResult(String tableName, List<T> generatedList) {
		super();
		this.tableName = tableName;
		this.generatedList = generatedList;
	}
	public String getTableName() {
		return tableName;
	}
	public List<T> getGeneratedList() {
		return generatedList;
	}
	@Override
	public String toString() {
		return "GeneratedResult [tableName=" + tableName + ", generatedList="
				+ generatedList + "]";
	}
	
}
