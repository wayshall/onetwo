package org.onetwo.dbm.event;

import org.onetwo.dbm.exception.EntityInsertException;
import org.onetwo.dbm.mapping.DbmMappedEntry;
import org.springframework.dao.DuplicateKeyException;

public class DbmInsertOrUpdateListener extends AbstractDbmEventListener {

	@Override
	protected int onInnerEventWithSingle(Object entity, DbmEvent event){
		DbmInsertOrUpdateEvent insertOrUpdate = (DbmInsertOrUpdateEvent)event;
		DbmEventSource es = insertOrUpdate.getEventSource();
		DbmMappedEntry entry = es.getMappedEntryManager().getEntry(entity);
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
