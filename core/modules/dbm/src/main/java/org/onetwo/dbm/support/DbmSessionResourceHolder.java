package org.onetwo.dbm.support;

import java.sql.Connection;

import org.springframework.transaction.support.ResourceHolderSupport;

public class DbmSessionResourceHolder extends ResourceHolderSupport {
	
	final private DbmSession session;
	final private Connection connection;

	public DbmSessionResourceHolder(DbmSession session, Connection connection){
		this.session = session;
		this.connection = connection;
	}

	public DbmSession getSession() {
		return session;
	}

	public DbmSessionFactory getSessionFactory() {
		return session.getSessionFactory();
	}

	public Connection getConnection() {
		return connection;
	}

}
