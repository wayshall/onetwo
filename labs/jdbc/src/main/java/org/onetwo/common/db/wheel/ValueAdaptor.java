package org.onetwo.common.db.wheel;

import org.onetwo.common.db.sql.Condition;

public interface ValueAdaptor {
	
	public Object getValue(Condition cond, DBConnection dbcon);

}