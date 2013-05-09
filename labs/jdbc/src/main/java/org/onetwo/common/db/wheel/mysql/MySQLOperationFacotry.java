package org.onetwo.common.db.wheel.mysql;

import org.onetwo.common.db.wheel.DefaultEntityOperationFactory;
import org.onetwo.common.db.wheel.EntityBuilder;
import org.onetwo.common.db.wheel.EntityBuilderFactory;
import org.onetwo.common.db.wheel.AbstractEntityOperation;
import org.onetwo.common.db.wheel.JDaoSupport;

public class MySQLOperationFacotry extends DefaultEntityOperationFactory {

	public MySQLOperationFacotry(EntityBuilderFactory entityBuilderFactory) {
		super(entityBuilderFactory);
	}

	protected AbstractEntityOperation createInsertEntityOperation(JDaoSupport dao, EntityBuilder builder){
		return new MySQLInsertEntityOperation(dao, builder);
	}

}
