package org.onetwo.common.jfishdbm.mapping;

import org.onetwo.common.jfishdbm.utils.Initializable;

public interface MappedEntryManager extends Initializable {
	
//	public boolean isSupported(Object entity);
	public boolean isSupportedMappedEntry(Object entity);
	public void scanPackages(String... packagesToScan);
	public JFishMappedEntry findEntry(Object object);
	public JFishMappedEntry getEntry(Object object);
//	public JFishMappedEntry buildMappedEntry(Class<?> entityClass, boolean byProperty);
	

}