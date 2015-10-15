package org.onetwo.common.jfishdbm.support;

import javax.sql.DataSource;
import javax.validation.Validator;

final public class DaoFactory {
	
	
	private DaoFactory(){
	}
	

	public static JFishDao newDao(DataSource dataSource){
		return newDao(dataSource, null);
	}
	
	public static JFishDao newDao(DataSource dataSource, Validator validator){
		JFishDaoImpl dao = new JFishDaoImpl(dataSource);
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
