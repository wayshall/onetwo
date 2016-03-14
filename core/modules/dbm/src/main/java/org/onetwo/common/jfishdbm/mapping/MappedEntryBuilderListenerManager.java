package org.onetwo.common.jfishdbm.mapping;

import java.util.Collection;

import org.onetwo.common.utils.LangUtils;

public class MappedEntryBuilderListenerManager {

	private Collection<MappedEntryBuilderListener> mappedEntryBuilderListener;

	public MappedEntryBuilderListenerManager(Collection<MappedEntryBuilderListener> mappedEntryBuilderListener) {
		super();
		this.mappedEntryBuilderListener = mappedEntryBuilderListener;
	}
	
	public void notifyAfterCreatedMappedEntry(JFishMappedEntry entry){
		if(LangUtils.isEmpty(mappedEntryBuilderListener))
			return ;
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterCreatedMappedEntry(entry);
		}
	}
	
	public void notifyAfterBuildMappedField(JFishMappedEntry entry, DbmMappedField mfield){
		if(LangUtils.isEmpty(mappedEntryBuilderListener))
			return ;
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterBuildMappedField(entry, mfield);
		}
	}
	
	public void notifyAfterBuildMappedEntry(JFishMappedEntry entry){
		if(LangUtils.isEmpty(mappedEntryBuilderListener))
			return ;
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterBuildMappedEntry(entry);
		}
	}
	
	
}
