package org.onetwo.common.fish.orm;

public class DefaultDataBaseConfig implements DataBaseConfig {

	@Override
	public int getUserBatchThreshold() {
		return 50;
	}

	@Override
	public boolean isBatchEnabled() {
		return true;
	}

	public int getProcessSizePerBatch() {
		return 1000;
	}

}
