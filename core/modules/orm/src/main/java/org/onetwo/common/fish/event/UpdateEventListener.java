package org.onetwo.common.fish.event;

import org.onetwo.common.fish.orm.JFishMappedEntry;

abstract public class UpdateEventListener extends AbstractJFishEventListener {
	
	@Override
	public void doEvent(JFishEvent event) {
		Object entity = event.getObject();
		JFishEventSource es = event.getEventSource();
		JFishMappedEntry entry = es.getMappedEntryManager().getEntry(entity);

		this.executeJFishEntityListener(true, event, entity, entry.getEntityListeners());
		this.doUpdate((JFishUpdateEvent)event, entry);
		this.executeJFishEntityListener(true, event, entity, entry.getEntityListeners());
	}

	abstract protected void doUpdate(JFishUpdateEvent event, JFishMappedEntry entry);

}
