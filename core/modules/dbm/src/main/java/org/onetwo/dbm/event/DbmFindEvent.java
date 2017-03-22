package org.onetwo.dbm.event;

public class DbmFindEvent extends DbmSessionEvent {

	public DbmFindEvent(Object object, DbmSessionEventSource eventSource) {
		super(object, DbmEventAction.find, eventSource);
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
