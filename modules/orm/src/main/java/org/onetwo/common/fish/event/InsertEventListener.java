package org.onetwo.common.fish.event;

import java.io.Serializable;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.utils.LangUtils;

abstract public class InsertEventListener extends AbstractJFishEventListener {
	
	@Override
	public void doEvent(JFishEvent event) {
		this.onInsert((JFishInsertEvent)event);
	}

	public void onInsert(JFishInsertEvent event) {
		Object entity = event.getObject();
		JFishEventSource es = event.getEventSource();
		JFishMappedEntry entry = es.getMappedEntryManager().getEntry(entity);
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
	
	abstract protected void doInsert(JFishInsertEvent event, JFishMappedEntry entry);

	
	/*protected int invokeInsert(JFishInsertEvent event, String sql, List<Object[]> args, JFishEventSource es){
		return executeJdbcUpdate(event, sql, args, es);
	}*/
	
}
