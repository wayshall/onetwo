package org.onetwo.dbm.mapping;

public interface MappedEntryBuilderListener {

	public void afterCreatedMappedEntry(DbmMappedEntry entry);
	
	public void afterBuildMappedField(DbmMappedEntry entry, DbmMappedField mfield);
	
	public void afterBuildMappedEntry(DbmMappedEntry entry);
}
