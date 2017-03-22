package org.onetwo.dbm.event;

import org.onetwo.dbm.mapping.DbmMappedEntry;

abstract public class UpdateEventListener extends AbstractDbmEventListener {
	
	@Override
	public void doEvent(DbmSessionEvent event) {
		Object entity = event.getObject();
		DbmSessionEventSource es = event.getEventSource();
		DbmMappedEntry entry = es.getMappedEntryManager().getEntry(entity);

		this.executeJFishEntityListener(true, event, entity, entry.getEntityListeners());
		this.doUpdate((DbmUpdateEvent)event, entry);
		this.executeJFishEntityListener(true, event, entity, entry.getEntityListeners());
	}

	abstract protected void doUpdate(DbmUpdateEvent event, DbmMappedEntry entry);

}
