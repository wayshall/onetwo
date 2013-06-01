package org.onetwo.common.fish.spring;

import java.util.List;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.fish.orm.MappedEntryManagerListener;
import org.onetwo.common.utils.LangUtils;

public class MappedEntryListenerManager {

	private List<MappedEntryManagerListener> mappedEntryBuilderEvents;
	private MappedEntryManager mappedEntryManager;

	public MappedEntryListenerManager(MappedEntryManager mappedEntryManager) {
		super();
		this.mappedEntryManager = mappedEntryManager;
	}
	

	public void fireBeforeBuildEvents(List<ScanedClassContext> entryClassNameList) {
		if(LangUtils.isEmpty(mappedEntryBuilderEvents))
			return ;

		for (MappedEntryManagerListener mbe : mappedEntryBuilderEvents) {
			mbe.beforeBuild(mappedEntryManager, entryClassNameList);
		}
	}

	public void fireAfterBuildEvents(JFishMappedEntry entry) {
		if(LangUtils.isEmpty(mappedEntryBuilderEvents))
			return ;

		for (MappedEntryManagerListener mbe : mappedEntryBuilderEvents) {
			mbe.afterBuilt(mappedEntryManager, entry);
//			if(toBuild){
//				buildEntry(entry);
//			}
		}
		
	}

	public void fireAfterAllEntriesHaveBuiltEvents(List<JFishMappedEntry> entryList) {
		if(LangUtils.isEmpty(mappedEntryBuilderEvents))
			return ;

		for (MappedEntryManagerListener mbe : mappedEntryBuilderEvents) {
			mbe.afterAllEntryHasBuilt(mappedEntryManager, entryList);
		}
//		if(toBuild){
//			for(JFishMappedEntry entry : entryList){
//				buildEntry(entry);
//			}
//		}
	}

	public void setMappedEntryBuilderEvents(List<MappedEntryManagerListener> mappedEntryBuilderEvent) {
		this.mappedEntryBuilderEvents = mappedEntryBuilderEvent;
	}

	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	
	
}
