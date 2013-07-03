package org.onetwo.common.spring.mcache;

import java.io.Serializable;

abstract public class AbstractCacheAdapter implements CacheAdapter {

	private String name;
	
	public AbstractCacheAdapter(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void put(Serializable key, Object value, int expire) {
		CacheElement ele = CacheElement.create(key, value, expire);
		put(ele);
	}
}
