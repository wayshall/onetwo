package org.onetwo.common.db.wheel;

import org.onetwo.common.db.wheel.AbstractEntityOperation.EntityOperationType;

public interface EntityOperationFactory {

	public AbstractEntityOperation create(JDaoSupport dao, EntityOperationType operation, Object entity);
	public AbstractEntityOperation create(JDaoSupport dao, EntityOperationType operation, TableInfo tableInfo);

}