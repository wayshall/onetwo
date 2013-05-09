package org.onetwo.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.IdEntity;
import org.onetwo.common.utils.Page;

@SuppressWarnings({"hiding", "unchecked"})
abstract public class BaseServiceImpl<T extends IdEntity, PK extends Serializable> implements BaseService<T, PK>{
	
	abstract public BaseDao getDao();

	@Override
	public Number countRecord(Map<Object, Object> properties) {
		return getDao().countRecord(properties);
	}

	@Override
	public Number countRecord(Object... params) {
		return getDao().countRecord(params);
	}

	@Override
	public <T extends ILogicDeleteEntity> void delete(T entity) {
		getDao().delete(entity);
	}

	@Override
	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass, PK id){
		return (T)getDao().deleteById(entityClass, id);
	}

	@Override
	public List findAll() {
		return getDao().findAll();
	}

	@Override
	public T findById(PK id) {
		return (T)getDao().findById(id);
	}

	@Override
	public List<T> findByProperties(Map<Object, Object> properties) {
		return getDao().findByProperties(properties);
	}

	@Override
	public List<T> findByProperties(Object... properties) {
		return getDao().findByProperties(properties);
	}

	@Override
	public void findPage(Page page, Map<Object, Object> properties) {
		getDao().findPage(page, properties);
	}

	@Override
	public void findPage(Page page, Object... properties) {
		getDao().findPage(page, properties);
	}

	@Override
	public T findUnique(Map<Object, Object> properties) {
		return (T)getDao().findUnique(properties);
	}

	@Override
	public T findUnique(Object... properties) {
		return (T)getDao().findUnique(properties);
	}

	/*@Override
	public T findUnique(String sql, Object... values) {
		return (T)getDao().findUnique(sql, values);
	}*/

	@Override
	public T load(PK id) {
		return (T)getDao().load(id);
	}

	@Override
	public void persist(T entity) {
		getDao().persist(entity);
	}

	@Override
	public void remove(T entity) {
		getDao().remove(entity);
	}

	@Override
	public T removeById(PK id) {
		return (T)getDao().removeById(id);
	}

	@Override
	public void save(T entity) {
		getDao().save(entity);
	}

}
