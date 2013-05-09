package org.onetwo.common.fish.event;

import org.onetwo.common.fish.orm.JFishMappedEntry;

public class JFishInsertOrUpdateListener extends AbstractJFishEventListener {

	@Override
	protected int onInnerEventWithSingle(Object entity, JFishEvent event){
		JFishInsertOrUpdateEvent insertOrUpdate = (JFishInsertOrUpdateEvent)event;
		JFishEventSource es = insertOrUpdate.getEventSource();
		JFishMappedEntry entry = es.getMappedEntryManager().getEntry(entity);
		int updateCount = 0;
		if(entry.hasIdentifyValue(entity)){
			if(insertOrUpdate.isDynamicUpdate())
				updateCount = es.dymanicUpdate(entity, insertOrUpdate.getRelatedFields());
			else
				updateCount = es.update(entity, insertOrUpdate.getRelatedFields());
		}else{
			updateCount = es.insert(entity, insertOrUpdate.getRelatedFields());
		}
		return updateCount;
	}

}
