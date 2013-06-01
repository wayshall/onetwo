package org.onetwo.common.fish.utils;

public class JdbcContext {
	
	private int operationCount=0;

	public int getOperationCount() {
		return operationCount;
	}

	public int increaseOperationCount() {
		this.operationCount++;
		return this.operationCount;
	}
	
}
