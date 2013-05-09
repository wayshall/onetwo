package org.onetwo.common.db.wheel.access;

import org.onetwo.common.db.wheel.ConnectionExecutor;
import org.onetwo.common.db.wheel.JPAEntityBuidable;
import org.onetwo.common.db.wheel.TableInfoBuilder;
import org.onetwo.common.db.wheel.Wheel;

public class AccessWheel extends Wheel {

	protected TableInfoBuilder createTableInfoBuilder(){
		return new JPAEntityBuidable();
	}

	@Override
	protected ConnectionExecutor createConnectionExecutor() {
		return new AccessConnectionExecutor();
	}
	
	
}
