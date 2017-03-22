package org.onetwo.dbm.event;

import java.util.Map;

public class DbmExtQueryEvent extends DbmSessionEvent {
	
	public enum ExtQueryType {
		DEFUALT,
		PAGINATION,
		UNIQUE,
		COUNT
	}

	private final Map<Object, Object> properties;
	private Object resultObject;
	private final ExtQueryType extQueryType;
	
	public DbmExtQueryEvent(ExtQueryType extQueryType, Class<?> entityClass, Map<Object, Object> properties, DbmSessionEventSource eventSource) {
		this(null, extQueryType, entityClass, properties, eventSource);
	}
	public DbmExtQueryEvent(Object obj, ExtQueryType extQueryType, Class<?> entityClass, Map<Object, Object> properties, DbmSessionEventSource eventSource) {
		super(obj, DbmEventAction.extQuery, eventSource);
		this.setEntityClass(entityClass);
		this.properties = properties;
		this.extQueryType = extQueryType;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

	public Map<Object, Object> getProperties() {
		return properties;
	}

	public ExtQueryType getExtQueryType() {
		return extQueryType;
	}

}
