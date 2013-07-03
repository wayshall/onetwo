package org.onetwo.common.fish.orm;

public interface MappedEntryManager {
	
	public boolean isSupported(Object entity);

	public JFishMappedEntry findEntry(Object object);
	public JFishMappedEntry getEntry(Object object);
//	public JFishMappedEntry buildMappedEntry(Class<?> entityClass, boolean byProperty);
	

}