package org.onetwo.common.fish.event;

public class JFishSaveRefEvent extends JFishEvent {

	final private boolean dropAllInFirst;//只对many side起作用
	
	public JFishSaveRefEvent(Object object, boolean dropAllInFirst, JFishEventSource eventSource) {
		super(object, JFishEventAction.saveRef, eventSource);
		this.dropAllInFirst = dropAllInFirst;
	}

	public boolean isDropAllInFirst() {
		return dropAllInFirst;
	}

}
