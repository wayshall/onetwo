package org.onetwo.common.fish.event;

public class JFishUpdateEvent extends JFishEvent{

	private boolean dynamicUpdate;
	private boolean batchUpdate;
	
	public JFishUpdateEvent(Object object, JFishEventSource eventSource) {
		super(object, JFishEventAction.update, eventSource);
	}
	
	public boolean isDynamicUpdate() {
		return dynamicUpdate;
	}

	public void setDynamicUpdate(boolean dynamicUpdate) {
		this.dynamicUpdate = dynamicUpdate;
	}

	public boolean isBatchUpdate() {
		return batchUpdate;
	}

	public void setBatchUpdate(boolean batchUpdate) {
		this.batchUpdate = batchUpdate;
	}
	
}
