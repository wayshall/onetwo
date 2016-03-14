package org.onetwo.common.jfishdbm.mapping;

public interface MappedEntryBuilderListener {

	public void afterCreatedMappedEntry(JFishMappedEntry entry);
	
	public void afterBuildMappedField(JFishMappedEntry entry, DbmMappedField mfield);
	
	public void afterBuildMappedEntry(JFishMappedEntry entry);
}
