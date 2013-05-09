package org.onetwo.common.db;


public interface IWithResourceEntity {

	public Long[] getResourceIds();
	
	public void setResourceIds(Long[] resourceIds);
	
	public Class<? extends IResourceEntity> getResourceClass();
}
