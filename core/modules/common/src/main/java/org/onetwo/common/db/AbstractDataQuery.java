package org.onetwo.common.db;

abstract public class AbstractDataQuery implements DataQuery{

	private boolean cacheable;

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
	

}
