package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.jfishdbm.exception.EntityInsertException;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.springframework.dao.DuplicateKeyException;

public class JFishInsertOrUpdateListener extends AbstractJFishEventListener {

	@Override
	protected int onInnerEventWithSingle(Object entity, JFishEvent event){
		JFishInsertOrUpdateEvent insertOrUpdate = (JFishInsertOrUpdateEvent)event;
		JFishEventSource es = insertOrUpdate.getEventSource();
		JFishMappedEntry entry = es.getMappedEntryManager().getEntry(entity);
		int updateCount = 0;
		
		if(entry.hasIdentifyValue(entity)){
			if(entry.getIdentifyField().isGeneratedValue()){
				if(insertOrUpdate.isDynamicUpdate()){
					updateCount = es.dymanicUpdate(entity);
				}else{
					updateCount = es.update(entity);
				}
			}else{
				try {
					es.insert(entity);
				} catch (EntityInsertException | DuplicateKeyException e) {
					logger.warn("insert error, try to update...");
					if(insertOrUpdate.isDynamicUpdate()){
						updateCount = es.dymanicUpdate(entity);
					}else{
						updateCount = es.update(entity);
					}
				}
			}
		}else{
			updateCount = es.insert(entity);
		}
		return updateCount;
	}
	

}
