package org.onetwo.dbm.support;

import java.io.Serializable;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.onetwo.common.db.BaseCrudEntityManager;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.CrudEntityManager;

final public class Dbms {
	
	
	private Dbms(){
	}
	

	public static <E, ID  extends Serializable> CrudEntityManager<E, ID> newCrudManager(Class<E> entityClass){
		return new BaseCrudEntityManager<>(entityClass);
	}
	public static <E, ID  extends Serializable> CrudEntityManager<E, ID> newCrudManager(Class<E> entityClass, BaseEntityManager baseEntityManager){
		return new BaseCrudEntityManager<>(entityClass, baseEntityManager);
	}
	
	public static DbmDao newDao(DataSource dataSource){
		return newDao(dataSource, null);
	}
	
	public static DbmDao newDao(DataSource dataSource, Validator validator){
		DbmDaoImpl dao = new DbmDaoImpl(dataSource);
		dao.setServiceRegistry(SimpleDbmInnserServiceRegistry.createServiceRegistry(dataSource, validator));
		dao.initialize();
		return dao;
	}

}
