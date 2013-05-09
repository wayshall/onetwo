package org.onetwo.common.db.event;

public class DefaultEvent {
	
	private Object object;
	protected EntityAction action;
	private EventSource eventSource;

	public DefaultEvent(Object object, EventSource eventSource) {
		super();
		this.object = object;
		this.eventSource = eventSource;
		this.action = EntityAction.Remove;
	}

	public Object getObject() {
		return object;
	}

	public EventSource getEventSource() {
		return eventSource;
	}

	public EntityAction getAction() {
		return action;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
