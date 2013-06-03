package org.onetwo.common.db.event;

public class RemoveEvent extends DefaultEvent {

	public RemoveEvent(Object object, EventSource eventSource) {
		super(object, eventSource);
		this.action = EntityAction.Remove;
	}

}
