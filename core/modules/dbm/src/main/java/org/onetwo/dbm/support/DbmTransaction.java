package org.onetwo.dbm.support;


public interface DbmTransaction {
	
//	Connection getConnection();
	
	void commit();
	
	void rollback();
	

}
