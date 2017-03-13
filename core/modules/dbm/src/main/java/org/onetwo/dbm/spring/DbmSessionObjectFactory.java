package org.onetwo.dbm.spring;

import org.onetwo.dbm.support.DbmSession;
import org.onetwo.dbm.support.DbmSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

public class DbmSessionObjectFactory implements ObjectFactory<DbmSession>{

	private DbmSessionFactory sessionFactory;
	
	public DbmSessionObjectFactory(DbmSessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	@Override
	public DbmSession getObject() throws BeansException {
		return sessionFactory.getCurrentSession();
	}
	
	

}
