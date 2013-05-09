package org.onetwo.common.db.event;

public class SaveOrUpdateEvent extends DefaultEvent {

	public SaveOrUpdateEvent(Object object, EventSource eventSource) {
		super(object, eventSource);
		this.action = EntityAction.SaveOrUpdate;
	}

}
