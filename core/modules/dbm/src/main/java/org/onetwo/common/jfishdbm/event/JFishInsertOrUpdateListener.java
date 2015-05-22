package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;

public class JFishInsertOrUpdateListener extends AbstractJFishEventListener {

	@Override
	protected int onInnerEventWithSingle(Object entity, JFishEvent event){
		JFishInsertOrUpdateEvent insertOrUpdate = (JFishInsertOrUpdateEvent)event;
		JFishEventSource es = insertOrUpdate.getEventSource();
		JFishMappedEntry entry = es.getMappedEntryManager().getEntry(entity);
		int updateCount = 0;
		if(entry.hasIdentifyValue(entity)){
			if(insertOrUpdate.isDynamicUpdate())
				updateCount = es.dymanicUpdate(entity);
			else
				updateCount = es.update(entity);
		}else{
			updateCount = es.insert(entity);
		}
		return updateCount;
	}

}
