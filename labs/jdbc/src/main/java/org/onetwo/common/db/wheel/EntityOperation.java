package org.onetwo.common.db.wheel;

import org.onetwo.common.db.wheel.AbstractEntityOperation.EntityOperationType;

public interface EntityOperation {
	

	public EntityOperation build();

	public EntityOperation dynamicBuild();

	public void execute();

	public Object getOperationObject();

	public void setOperationObject(Object operationObject);

	public Object getResult();

	public Object getSingleResult();

	public EntityOperationType getOperationType();

}