package org.onetwo.common.jfishdbm.event;

public class JFishInsertOrUpdateEvent extends JFishEvent {

	private final boolean dynamicUpdate;
	

	public JFishInsertOrUpdateEvent(Object object, JFishEventSource eventSource) {
		this(object, true, eventSource);
	}
	
	public JFishInsertOrUpdateEvent(Object object, boolean dynamicUpdate, JFishEventSource eventSource) {
		super(object, JFishEventAction.insertOrUpdate, eventSource);
		this.dynamicUpdate = dynamicUpdate;
	}

	public boolean isDynamicUpdate() {
		return dynamicUpdate;
	}

}
