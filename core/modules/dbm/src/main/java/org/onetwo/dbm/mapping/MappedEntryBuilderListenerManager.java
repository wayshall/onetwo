package org.onetwo.dbm.mapping;

import java.util.Collection;

import org.onetwo.common.utils.LangUtils;

public class MappedEntryBuilderListenerManager {

	private Collection<MappedEntryBuilderListener> mappedEntryBuilderListener;

	public MappedEntryBuilderListenerManager(Collection<MappedEntryBuilderListener> mappedEntryBuilderListener) {
		super();
		this.mappedEntryBuilderListener = mappedEntryBuilderListener;
	}
	
	public void notifyAfterCreatedMappedEntry(DbmMappedEntry entry){
		if(LangUtils.isEmpty(mappedEntryBuilderListener))
			return ;
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterCreatedMappedEntry(entry);
		}
	}
	
	public void notifyAfterBuildMappedField(DbmMappedEntry entry, DbmMappedField mfield){
		if(LangUtils.isEmpty(mappedEntryBuilderListener))
			return ;
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterBuildMappedField(entry, mfield);
		}
	}
	
	public void notifyAfterBuildMappedEntry(DbmMappedEntry entry){
		if(LangUtils.isEmpty(mappedEntryBuilderListener))
			return ;
		for(MappedEntryBuilderListener l : mappedEntryBuilderListener){
			l.afterBuildMappedEntry(entry);
		}
	}
	
	
}
