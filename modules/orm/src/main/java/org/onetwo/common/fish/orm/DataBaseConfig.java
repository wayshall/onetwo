package org.onetwo.common.fish.orm;

public class DataBaseConfig {
	private boolean batchEnabled = true;
	private int userBatchThreshold = 5;

	public int getUserBatchThreshold() {
		return userBatchThreshold;
	}

	public boolean isBatchEnabled() {
		return batchEnabled;
	}

}
