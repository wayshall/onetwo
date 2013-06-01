package org.onetwo.common.spring.cache;

public interface SimpleCacheManager {

	public SimpleCacheWrapper getSimpleCacheWrapper(String name);

	public SimpleCacheWrapper getExcelTemplateCache();

}