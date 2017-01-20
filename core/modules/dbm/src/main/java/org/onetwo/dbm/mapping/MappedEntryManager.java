package org.onetwo.dbm.mapping;


public interface MappedEntryManager {
	
//	public boolean isSupported(Object entity);
	public boolean isSupportedMappedEntry(Object entity);
	public void scanPackages(String... packagesToScan);
	public JFishMappedEntry findEntry(Object object);
	public JFishMappedEntry getEntry(Object object);
	public void setMappedEntryManagerListener(MappedEntryManagerListener mappedEntryManagerListener);
//	public JFishMappedEntry buildMappedEntry(Class<?> entityClass, boolean byProperty);
	

}