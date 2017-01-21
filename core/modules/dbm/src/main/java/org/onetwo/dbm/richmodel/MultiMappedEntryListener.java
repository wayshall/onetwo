package org.onetwo.dbm.richmodel;

import java.util.List;

import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.mapping.MappedEntryManagerListener;
import org.onetwo.dbm.mapping.ScanedClassContext;

import com.google.common.collect.Lists;

public class MultiMappedEntryListener implements MappedEntryManagerListener {
	
	private List<MappedEntryManagerListener> listeners = Lists.newArrayList();
	
	public void addListener(MappedEntryManagerListener listener){
		this.listeners.add(listener);
	}

	@Override
	public void beforeBuild(MappedEntryManager mappedEntryManager, List<ScanedClassContext> clssNameList) {
		listeners.forEach(l->l.beforeBuild(mappedEntryManager, clssNameList));
	}
	
	

}
