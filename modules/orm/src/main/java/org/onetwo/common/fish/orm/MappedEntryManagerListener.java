package org.onetwo.common.fish.orm;

import java.util.List;

import org.onetwo.common.fish.spring.ScanedClassContext;


public interface MappedEntryManagerListener {

//	public void afterCompleteBuildMappedEntry(JFishMappedEntry entry);

	public void beforeBuild(MappedEntryManager mappedEntryManager, List<ScanedClassContext> clssNameList);
	
	public void afterBuilt(MappedEntryManager mappedEntryManager, JFishMappedEntry entry);
	
	/*********
	 * only invoke on scan model
	 * 
	 * @param mappedEntryManager
	 * @param entryList
	 */
	public void afterAllEntryHasBuilt(MappedEntryManager mappedEntryManager, List<JFishMappedEntry> entryList);
	

}