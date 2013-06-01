package org.onetwo.common.fish.event;

public class JFishDropRefEvent extends JFishEvent {

	final private boolean dropAllRef;
	

	public JFishDropRefEvent(Object object, JFishEventSource eventSource) {
		this(object, false, eventSource);
	}
	public JFishDropRefEvent(Object object, boolean dropAllRef, JFishEventSource eventSource) {
		super(object, JFishEventAction.dropRef, eventSource);
		this.dropAllRef = dropAllRef;
	}

	public boolean isDropAllRef() {
		return dropAllRef;
	}


}
