package org.onetwo.common.jfishdb.orm;

public interface MappedEntryBuilderListener {

	public void afterCreatedMappedEntry(JFishMappedEntry entry);
	
	public void afterBuildMappedField(JFishMappedEntry entry, JFishMappedField mfield);
	
	public void afterBuildMappedEntry(JFishMappedEntry entry);
}
