package org.onetwo.common.jfishdbm.event;

public interface DbmEntityFieldListener {
	
	public Object beforeFieldInsert(String fieldName, Object value);
	
	public Object beforeFieldUpdate(String fieldName, Object value);
	
}
