package org.onetwo.dbm.support;

import org.springframework.transaction.support.ResourceHolderSupport;

public class DbmSessionResourceHolder extends ResourceHolderSupport {
	
	final private DbmSession session;

	public DbmSessionResourceHolder(DbmSession session){
		this.session = session;
	}

	public DbmSession getSession() {
		return session;
	}

	public DbmSessionFactory getSessionFactory() {
		return session.getSessionFactory();
	}

}
