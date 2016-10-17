package org.onetwo.dbm.support;

import javax.sql.DataSource;
import javax.validation.Validator;

final public class Dbms {
	
	
	private Dbms(){
	}
	

	public static DbmDao newDao(DataSource dataSource){
		return newDao(dataSource, null);
	}
	
	public static DbmDao newDao(DataSource dataSource, Validator validator){
		DbmDaoImpl dao = new DbmDaoImpl(dataSource);
		dao.setServiceRegistry(createServiceRegistry(dataSource, validator));
		dao.initialize();
		return dao;
	}
	
	public static SimpleDbmInnserServiceRegistry createServiceRegistry(DataSource dataSource, Validator validator){
		SimpleDbmInnserServiceRegistry serviceRegistry = new SimpleDbmInnserServiceRegistry();
		serviceRegistry.initialize(dataSource, null);
		if(validator!=null){
			serviceRegistry.setEntityValidator(new Jsr303EntityValidator(validator));
		}
		return serviceRegistry;
	}

}
