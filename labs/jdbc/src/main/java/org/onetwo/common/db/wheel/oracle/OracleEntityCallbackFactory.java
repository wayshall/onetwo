package org.onetwo.common.db.wheel.oracle;

import org.onetwo.common.db.wheel.DefaultEntityOperationFactory;
import org.onetwo.common.db.wheel.EntityBuilder;
import org.onetwo.common.db.wheel.EntityBuilderFactory;
import org.onetwo.common.db.wheel.AbstractEntityOperation;
import org.onetwo.common.db.wheel.JDaoSupport;


public class OracleEntityCallbackFactory extends DefaultEntityOperationFactory {

	public OracleEntityCallbackFactory(EntityBuilderFactory entityBuilderFactory) {
		super(entityBuilderFactory);
	}

	@Override
	protected AbstractEntityOperation createInsertEntityOperation(JDaoSupport dao, EntityBuilder builder){
		return new OracleInsertEntityOperation(dao, builder);
	}

}
