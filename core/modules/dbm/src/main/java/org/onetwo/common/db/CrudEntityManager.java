package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.utils.Page;

public interface CrudEntityManager<T, PK extends Serializable> {
 
	public T load(PK id);
	
	public T findById(PK id);
	
	public T save(T entity);
	
//	public T createNew(T entity);

//	public T updateAttributes(T entity);

	public T remove(T entity);
	
	public void removes(Collection<T> entities);

	public T removeById(PK id);
	public List<T> removeByIds(PK[] id);

	public Collection<T> removeByIds(Class<T> entityClass, PK[] ids);

	public List<T> findAll();

	public Number countRecord(Map<Object, Object> properties);

	public Number countRecord(Object... params);

	public List<T> findByProperties(Object... properties);

	public List<T> findByProperties(Map<Object, Object> properties);
	
	public List<T> findByProperties(QueryBuilder squery);

	public Page<T> findPage(final Page<T> page, Object... properties);

	public Page<T> findPage(final Page<T> page, Map<Object, Object> properties);
	
	public void findPage(final Page<T> page, QueryBuilder query);

	public T findUnique(Object... properties);
	
	public T findUnique(Map<Object, Object> properties);
	
	public T findOne(Object... properties);

}
