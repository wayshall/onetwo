package org.onetwo.common.ejb.jpa;

import java.io.Serializable;

import javax.ejb.EJB;

import org.onetwo.common.db.BaseCrudServiceImpl;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.utils.ReflectUtils;

@SuppressWarnings("rawtypes")
abstract public class AbstractCrudServiceImpl<T, PK extends Serializable> extends BaseCrudServiceImpl<T, PK> {

	protected BaseEntityManager baseEntityManager;

	protected Class entityClass;
	
	public AbstractCrudServiceImpl(){
		this.entityClass = ReflectUtils.getSuperClassGenricType(this.getClass(), AbstractCrudServiceImpl.class);
	}
	
	@EJB
	public void setBaseEntityManager(BaseEntityManager baseEntityManager) {
		this.baseEntityManager = baseEntityManager;
	}
	

	@Override
	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}


}
