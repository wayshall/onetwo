package org.onetwo.common.fish.utils;

public class ProfilerContext {
	
	private int operationCount=0;

	public int getOperationCount() {
		return operationCount;
	}

	public int increaseOperationCount() {
		this.operationCount++;
		return this.operationCount;
	}
	
}
