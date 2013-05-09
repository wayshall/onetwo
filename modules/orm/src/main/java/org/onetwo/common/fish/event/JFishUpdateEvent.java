package org.onetwo.common.fish.event;

public class JFishUpdateEvent extends JFishEvent{

	private boolean dynamicUpdate;
	
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
