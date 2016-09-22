package org.onetwo.dbm.event;

public class JFishInsertEvent extends JFishEvent{

	private boolean fetchId;
	
	public JFishInsertEvent(Object object, JFishEventSource eventSource) {
		super(object, JFishEventAction.insert, eventSource);
	}

	public boolean isFetchId() {
		return fetchId;
	}

	public void setFetchId(boolean fetchId) {
		this.fetchId = fetchId;
	}

}
