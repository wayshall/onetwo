package org.onetwo.common.spring.utils;

import java.util.Collection;

import org.springframework.core.io.Resource;

public interface ResourcesScanner {

	public JFishResourcesScanner CLASS_CANNER = new JFishResourcesScanner(JFishResourcesScanner.DEFAULT_RESOURCE_PATTERN);

	public Collection<Class<?>> scanClasses(String... packagesToScan);
	public Collection<Resource> scanResources(String... packagesToScan);

	public <T> Collection<T> scan(ScanResourcesCallback<T> filter, String... packagesToScan);
	public <T> Collection<T> scan(boolean readMetaData, ScanResourcesCallback<T> filter, String... packagesToScan);

}