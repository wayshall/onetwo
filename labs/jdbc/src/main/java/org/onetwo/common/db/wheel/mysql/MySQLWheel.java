package org.onetwo.common.db.wheel.mysql;

import org.onetwo.common.db.wheel.EntityBuilderFactory;
import org.onetwo.common.db.wheel.EntityOperationFactory;
import org.onetwo.common.db.wheel.JPAEntityBuidable;
import org.onetwo.common.db.wheel.TableInfoBuilder;
import org.onetwo.common.db.wheel.Wheel;

public class MySQLWheel extends Wheel {

	protected TableInfoBuilder createTableInfoBuilder(){
		return new JPAEntityBuidable();
	}

	protected EntityOperationFactory createEntityOperationFactory(EntityBuilderFactory entityBuilderFactory){
		return new MySQLOperationFacotry(entityBuilderFactory);
	}
	
}
