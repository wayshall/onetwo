package org.onetwo.common.db.wheel;

import org.onetwo.common.db.sql.AnotherQuery;

public interface EnhanceQuery extends AnotherQuery {
	
	public static interface EventListener {
		void onBefore(DBConnection dbcon);
		void onAfter(DBConnection dbcon, Object result);
	}
	
	public static class EventListenerAdaptor implements EventListener {

		@Override
		public void onAfter(DBConnection dbcon, Object result) {
			
		}

		@Override
		public void onBefore(DBConnection dbcon) {
		}
		
	}
	
	public static enum SqlOperation {
		query,
		batch,
		update
	}

	public void onBefore(SqlOperation operation, DBConnection dbcon);
	
	public void onAfter(SqlOperation operation, DBConnection dbcon, Object result);
	public AnotherQuery registerListener(SqlOperation operation, EventListener listener);
	public SqlOperation getSqlOperation();
	public void setSqlOperation(SqlOperation sqlType);
	public void addBath();
}
