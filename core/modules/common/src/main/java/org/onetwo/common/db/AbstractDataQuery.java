package org.onetwo.common.db;

import org.onetwo.common.utils.Page;

abstract public class AbstractDataQuery implements DataQuery{

	private boolean cacheable;

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
	
	@SuppressWarnings("rawtypes")
	public DataQuery setPageParameter(final Page page) {
		if(!page.isPagination())
			return this;
		return setLimited(page.getFirst()-1, page.getPageSize());
	}
	

}
