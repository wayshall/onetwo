package org.onetwo.dbm.event;

public class DbmInsertOrUpdateEvent extends DbmEvent {

	private final boolean dynamicUpdate;
	

	public DbmInsertOrUpdateEvent(Object object, DbmEventSource eventSource) {
		this(object, true, eventSource);
	}
	
	public DbmInsertOrUpdateEvent(Object object, boolean dynamicUpdate, DbmEventSource eventSource) {
		super(object, DbmEventAction.insertOrUpdate, eventSource);
		this.dynamicUpdate = dynamicUpdate;
	}

	public boolean isDynamicUpdate() {
		return dynamicUpdate;
	}

}
