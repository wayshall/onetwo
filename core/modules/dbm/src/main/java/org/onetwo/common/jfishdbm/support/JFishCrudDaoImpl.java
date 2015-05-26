package org.onetwo.common.jfishdbm.support;

import java.io.Serializable;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseCrudServiceImpl;
import org.onetwo.common.jfishdbm.query.JFishQueryBuilder;

abstract public class JFishCrudDaoImpl<T, PK extends Serializable> extends BaseCrudServiceImpl<T, PK> {

	private JFishEntityManager baseEntityManager;
	
	public JFishCrudDaoImpl(){
	}
	
	@Resource
	public void setBaseEntityManager(JFishEntityManager baseEntityManager) {
		this.baseEntityManager = baseEntityManager;
	}


	@Override
	public JFishEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}
	
	public JFishQueryBuilder createQueryBuilder(){
		return getBaseEntityManager().createQueryBuilder(entityClass);
	}


}
