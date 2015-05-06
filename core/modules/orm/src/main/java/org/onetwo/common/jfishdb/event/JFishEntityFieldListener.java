package org.onetwo.common.jfishdb.event;

public interface JFishEntityFieldListener {
	
	public Object beforeFieldInsert(String fieldName, Object value);
	
	public Object beforeFieldUpdate(String fieldName, Object value);
	
}
