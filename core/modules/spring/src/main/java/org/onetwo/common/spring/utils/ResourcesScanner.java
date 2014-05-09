package org.onetwo.common.spring.utils;

import java.util.List;

import org.springframework.core.io.Resource;

public interface ResourcesScanner {

	public JFishResourcesScanner CLASS_CANNER = new JFishResourcesScanner(JFishResourcesScanner.DEFAULT_RESOURCE_PATTERN);

	public List<Class<?>> scanClasses(String... packagesToScan);
	public List<Resource> scanResources(String... packagesToScan);

	public <T> List<T> scan(ScanResourcesCallback<T> filter, String... packagesToScan);
	public <T> List<T> scan(boolean readMetaData, ScanResourcesCallback<T> filter, String... packagesToScan);

}