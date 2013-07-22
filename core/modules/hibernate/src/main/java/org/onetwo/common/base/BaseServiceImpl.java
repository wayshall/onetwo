package org.onetwo.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.springframework.transaction.annotation.Transactional;

abstract public class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK>{

	protected Class<T> entityClass;
	
	public BaseServiceImpl(){
		this.entityClass = ReflectUtils.getSuperClassGenricType(this.getClass());
	}
	abstract public BaseEntityManager getBaseEntityManager();

	@Transactional
	@Override
	public Number countRecord(Map<Object, Object> properties) {
		return getBaseEntityManager().countRecord(entityClass, properties);
	}

	@Transactional
	@Override
	public Number countRecord(Object... params) {
		return getBaseEntityManager().countRecord(entityClass, params);
	}


	@Transactional
	@Override
	public List<T> findAll() {
		return getBaseEntityManager().findAll(entityClass);
	}

	@Transactional
	@Override
	public T findById(PK id) {
		return (T)getBaseEntityManager().findById(entityClass, id);
	}

	@Transactional
	@Override
	public List<T> findByProperties(Map<Object, Object> properties) {
		return getBaseEntityManager().findByProperties(entityClass, properties);
	}

	@Transactional
	@Override
	public List<T> findByProperties(Object... properties) {
		return getBaseEntityManager().findByProperties(entityClass, properties);
	}

	@Transactional
	@Override
	public void findPage(Page<T> page, Map<Object, Object> properties) {
		getBaseEntityManager().findPage(entityClass, page, properties);
	}

	@Transactional
	@Override
	public void findPage(Page<T> page, Object... properties) {
		getBaseEntityManager().findPage(entityClass, page, properties);
	}

	@Transactional
	@Override
	public T findUnique(Map<Object, Object> properties) {
		return (T)getBaseEntityManager().findUnique(entityClass, properties);
	}

	@Transactional
	@Override
	public T findUnique(Object... properties) {
		return (T)getBaseEntityManager().findUnique(entityClass, properties);
	}

	/*@Override
	public T findUnique(String sql, Object... values) {
		return (T)getDao().findUnique(sql, values);
	}*/

	@Transactional
	@Override
	public T load(PK id) {
		return (T)getBaseEntityManager().load(entityClass, id);
	}

	@Transactional
	@Override
	public void persist(T entity) {
		getBaseEntityManager().persist(entity);
	}

	@Override
	public void remove(T entity) {
		getBaseEntityManager().remove(entity);
	}

	@Transactional
	@Override
	public T removeById(PK id) {
		return (T)getBaseEntityManager().removeById(entityClass, id);
	}

	@Transactional
	@Override
	public void save(T entity) {
		getBaseEntityManager().save(entity);
	}

}
