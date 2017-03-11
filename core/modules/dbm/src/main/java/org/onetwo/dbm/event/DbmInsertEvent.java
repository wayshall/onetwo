package org.onetwo.dbm.event;

public class DbmInsertEvent extends DbmEvent{

	private boolean fetchId;
	
	public DbmInsertEvent(Object object, DbmEventSource eventSource) {
		super(object, DbmEventAction.insert, eventSource);
	}

	public boolean isFetchId() {
		return fetchId;
	}

	public void setFetchId(boolean fetchId) {
		this.fetchId = fetchId;
	}

}
