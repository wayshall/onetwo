package org.onetwo.dbm.event;

import java.io.Serializable;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.mapping.DbmMappedEntry;

abstract public class InsertEventListener extends AbstractDbmEventListener {
	
	@Override
	public void doEvent(DbmEvent event) {
		this.onInsert((DbmInsertEvent)event);
	}

	public void onInsert(DbmInsertEvent event) {
		Object entity = event.getObject();
		DbmEventSource es = event.getEventSource();
		DbmMappedEntry entry = es.getMappedEntryManager().getEntry(entity);
		event.setJoined(entry.isJoined());
		
		if(entry.isJoined()){
			Object findEntity = es.findById(entry.getEntityClass(), (Serializable)entity);
			if(findEntity!=null){
				this.logger.debug("joined entity["+LangUtils.toString(entity)+"] exist, ignore insert.");
				return ;
			}
		}
		
		this.executeJFishEntityListener(true, event, entity, entry.getEntityListeners());
		this.doInsert(event, entry);
		this.executeJFishEntityListener(false, event, entity, entry.getEntityListeners());
	}
	
	abstract protected void doInsert(DbmInsertEvent event, DbmMappedEntry entry);

	
	/*protected int invokeInsert(JFishInsertEvent event, String sql, List<Object[]> args, JFishEventSource es){
		return executeJdbcUpdate(event, sql, args, es);
	}*/
	
}
