package org.onetwo.common.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Page;

public interface CrudEntityManager<T, PK extends Serializable> {
 
	public T load(PK id);
	
	public T findById(PK id);
	
	public void persist(Object entity);

	public T save(T entity);

	public T remove(T entity);
	
	public void removeList(List<T> entities);

	public T removeById(PK id);

	public List<T> findAll();

	public Number countRecord(Map<Object, Object> properties);

	public Number countRecord(Object... params);

	public List<T> findByProperties(Object... properties);

	public List<T> findByProperties(Map<Object, Object> properties);

	public Page<T> findPage(final Page<T> page, Object... properties);

	public Page<T> findPage(final Page<T> page, Map<Object, Object> properties);

	public void delete(ILogicDeleteEntity entity);
	
	public ILogicDeleteEntity deleteById(Serializable id);

	public T findUnique(Object... properties);
	
	public T findUnique(boolean tryTheBest, Object... properties);
	
	public T findUnique(Map<Object, Object> properties);

}
