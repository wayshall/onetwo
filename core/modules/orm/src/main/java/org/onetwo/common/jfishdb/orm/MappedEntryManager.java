package org.onetwo.common.jfishdb.orm;

import org.onetwo.common.jfishdb.Initializable;

public interface MappedEntryManager extends Initializable {
	
//	public boolean isSupported(Object entity);
	public void scanPackages(String... packagesToScan);
	public JFishMappedEntry findEntry(Object object);
	public JFishMappedEntry getEntry(Object object);
//	public JFishMappedEntry buildMappedEntry(Class<?> entityClass, boolean byProperty);
	

}