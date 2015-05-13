package org.onetwo.common.jfishdbm.event;

import java.util.Map;

public class JFishExtQueryEvent extends JFishEvent {
	
	public enum ExtQueryType {
		DEFUALT,
		PAGINATION,
		UNIQUE,
		COUNT
	}

	private final Map<Object, Object> properties;
	private Object resultObject;
	private final ExtQueryType extQueryType;
	
	public JFishExtQueryEvent(ExtQueryType extQueryType, Class<?> entityClass, Map<Object, Object> properties, JFishEventSource eventSource) {
		this(null, extQueryType, entityClass, properties, eventSource);
	}
	public JFishExtQueryEvent(Object obj, ExtQueryType extQueryType, Class<?> entityClass, Map<Object, Object> properties, JFishEventSource eventSource) {
		super(obj, JFishEventAction.extQuery, eventSource);
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
