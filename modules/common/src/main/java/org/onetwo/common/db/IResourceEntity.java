package org.onetwo.common.db;



public interface IResourceEntity extends IdEntity<Long> {
	
	public void setEntityName(String entityName);

	public void setEntityId(Long entityId);
	public Long getEntityId();
	
}
