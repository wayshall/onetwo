package org.onetwo.dbm.core;

import java.io.Serializable;

import javax.persistence.Transient;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.CrudEntityManager;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.dbm.utils.Dbms;

abstract public class BaseModel<E, ID extends Serializable> {

	protected static BaseEntityManager entityManager(){
		return Dbms.obtainBaseEntityManager();
	}
	@Transient
	private Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseModel() {
		this.entityClass = (Class<E>)ReflectUtils.getSuperClassGenricType(this.getClass(), null);
	}
	protected CrudEntityManager<E, ID> obtainCrudManager(){
		return Dbms.obtainCrudManager(entityClass);
	}
	public void save(){
		entityManager().save(this);
	}
	public void update(){
		entityManager().update(this);
	}
	public void persist(){
		entityManager().persist(this);
	}
	public void remove(){
		entityManager().remove(this);
	}

}
