package org.onetwo.common.jfishdbm.support;

import java.io.Serializable;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseCrudServiceImpl;
import org.onetwo.common.jfishdbm.query.JFishQueryBuilder;

abstract public class DbmCrudDaoImpl<T, PK extends Serializable> extends BaseCrudServiceImpl<T, PK> {

	private DbmEntityManager baseEntityManager;
	
	public DbmCrudDaoImpl(){
	}
	
	@Resource
	public void setBaseEntityManager(DbmEntityManager baseEntityManager) {
		this.baseEntityManager = baseEntityManager;
	}


	@Override
	public DbmEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}
	
	public JFishQueryBuilder createQueryBuilder(){
		return getBaseEntityManager().createQueryBuilder(entityClass);
	}


}
