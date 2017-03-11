package org.onetwo.dbm.event;

public class DbmUpdateEvent extends DbmEvent{

	private boolean dynamicUpdate;
//	private boolean batchUpdate;
	
	public DbmUpdateEvent(Object object, DbmEventSource eventSource) {
		super(object, DbmEventAction.update, eventSource);
	}
	
	public boolean isDynamicUpdate() {
		return dynamicUpdate;
	}

	public void setDynamicUpdate(boolean dynamicUpdate) {
		this.dynamicUpdate = dynamicUpdate;
	}
	
}
