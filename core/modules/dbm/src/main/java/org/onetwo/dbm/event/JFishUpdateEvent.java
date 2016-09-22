package org.onetwo.dbm.event;

public class JFishUpdateEvent extends JFishEvent{

	private boolean dynamicUpdate;
//	private boolean batchUpdate;
	
	public JFishUpdateEvent(Object object, JFishEventSource eventSource) {
		super(object, JFishEventAction.update, eventSource);
	}
	
	public boolean isDynamicUpdate() {
		return dynamicUpdate;
	}

	public void setDynamicUpdate(boolean dynamicUpdate) {
		this.dynamicUpdate = dynamicUpdate;
	}
	
}
