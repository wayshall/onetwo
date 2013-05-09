package org.onetwo.common.fish.orm;

import java.util.List;

public class MappedEntryBuilderListenerManager {

	private List<MappedEntryBuilderListener> mappedEntryBuilderListener;

	public MappedEntryBuilderListenerManager(List<MappedEntryBuilderListener> mappedEntryBuilderListener) {
		super();
		this.mappedEntryBuilderListener = mappedEntryBuilderListener;
	}
	
	public void notifyAfterCreatedMappedEntry(JFishMappedEntry entry){
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterCreatedMappedEntry(entry);
		}
	}
	
	public void notifyAfterBuildMappedField(JFishMappedEntry entry, JFishMappedField mfield){
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterBuildMappedField(entry, mfield);
		}
	}
	
	public void notifyAfterBuildMappedEntry(JFishMappedEntry entry){
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterBuildMappedEntry(entry);
		}
	}
	
	
}
