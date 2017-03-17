package org.onetwo.dbm.support;

import java.sql.Connection;


public interface DbmTransaction {
	
	Connection getConnection();
	
	void commit();
	
	void rollback();
	

}
