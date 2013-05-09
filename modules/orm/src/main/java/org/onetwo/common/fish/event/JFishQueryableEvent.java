package org.onetwo.common.fish.event;

public class JFishQueryableEvent extends JFishEvent {

	public JFishQueryableEvent(Object object, JFishEventSource eventSource) {
		super(object, JFishEventAction.queryable, eventSource);
	}

	private Object resultObject;

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}
	
	
}
