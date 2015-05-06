package org.onetwo.common.jfishdb.event;

public interface JFishEntityListener {
	
	public void beforeInsert(Object entity);
	
	public void afterInsert(Object entity);
	
	public void beforeUpdate(Object entity);
	
	public void afterUpdate(Object entity);

}
