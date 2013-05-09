package org.onetwo.common.db.wheel.oracle;

import org.onetwo.common.db.wheel.EntityBuilderFactory;
import org.onetwo.common.db.wheel.EntityOperationFactory;
import org.onetwo.common.db.wheel.JPAEntityBuidable;
import org.onetwo.common.db.wheel.SQLBuilderFactory;
import org.onetwo.common.db.wheel.TableInfoBuilder;
import org.onetwo.common.db.wheel.Wheel;

public class OracleWheel extends Wheel {

	protected EntityOperationFactory createEntityOperationFactory(EntityBuilderFactory entityBuilderFactory){
		return new OracleEntityCallbackFactory(entityBuilderFactory);
	}
	protected TableInfoBuilder createTableInfoBuilder(){
		return new JPAEntityBuidable();
	}

	protected SQLBuilderFactory createSQLBuilderFactory(){
		return new OracleSQLBuilderFactory();
	}
}
