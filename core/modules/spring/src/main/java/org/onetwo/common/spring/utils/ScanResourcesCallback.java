package org.onetwo.common.spring.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;


public interface ScanResourcesCallback<T> {

//	public boolean isCandidate(MetadataReader metadataReader, Resource resource);
	
	public T doWithCandidate(MetadataReader metadataReader, Resource resource, int count);
}
