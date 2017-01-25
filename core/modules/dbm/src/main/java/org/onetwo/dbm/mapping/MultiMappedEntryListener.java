package org.onetwo.dbm.mapping;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

public class MultiMappedEntryListener implements MappedEntryManagerListener {
	
	private List<MappedEntryManagerListener> listeners = Lists.newArrayList();
	
	public void addListener(MappedEntryManagerListener listener){
		this.listeners.add(listener);
	}

	@Override
	public void beforeBuild(MappedEntryManager mappedEntryManager, Collection<ScanedClassContext> clssNameList) {
		listeners.forEach(l->l.beforeBuild(mappedEntryManager, clssNameList));
	}
	
	

}
