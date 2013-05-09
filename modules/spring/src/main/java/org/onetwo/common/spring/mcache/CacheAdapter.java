package org.onetwo.common.spring.mcache;

import java.io.Serializable;

public interface CacheAdapter {

	public String getName();

	public void put(Serializable key, Object value, int expire);
	public void put(CacheElement element);

	public CacheElement get(Serializable key);

	public void invalidate(Serializable key);
	public void invalidateAll();

//	public void refresh(Serializable key);

}