package org.onetwo.common.jfishdbm.event;

public class JFishFindEvent extends JFishEvent {

	public JFishFindEvent(Object object, JFishEventSource eventSource) {
		super(object, JFishEventAction.find, eventSource);
	}

	private Object resultObject;
	private boolean findAll;

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

	public boolean isFindAll() {
		return findAll;
	}

	public void setFindAll(boolean findAll) {
		this.findAll = findAll;
	}
	
}
