package org.onetwo.common.db.wheel;

import org.onetwo.common.db.wheel.EnhanceQuery.SqlOperation;

public abstract class EnhanceQueryFacotry {

	public static EnhanceQuery create(String sql){
		return new EnhanceQueryImpl(sql);
	}
	
	public static EnhanceQuery createUpdate(String sql){
		EnhanceQuery q = new EnhanceQueryImpl(sql);
		q.setSqlOperation(SqlOperation.update);
		return q;
	}
}
