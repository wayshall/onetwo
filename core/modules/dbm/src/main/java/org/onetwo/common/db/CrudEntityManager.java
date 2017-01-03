package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.utils.Page;

public interface CrudEntityManager<T, PK extends Serializable> {
 
	public T load(PK id);
	
	public T findById(PK id);
	
	public Optional<T> findOptionalById(PK id);

	/*****
	 * insert or update
	 * @param entity
	 * @return
	 */
	public T save(T entity);

	public void update(T entity);
	public void persist(T entity);
	
//	public T createNew(T entity);

//	public T updateAttributes(T entity);

	public T remove(T entity);
	
	public void removes(Collection<T> entities);

	public T removeById(PK id);
	public Collection<T> removeByIds(PK[] id);

	public int removeAll();

	public List<T> findAll();

	public Number countRecord(Map<Object, Object> properties);

	public Number countRecord(Object... params);

	public List<T> findListByProperties(Object... properties);

	public List<T> findListByProperties(Map<Object, Object> properties);
	
	public List<T> findListByProperties(QueryBuilder squery);
	
	public List<T> findListByExample(T example);

	public Page<T> findPage(final Page<T> page, Object... properties);

	public Page<T> findPage(final Page<T> page, Map<Object, Object> properties);
	
	public Page<T> findPageByExample(final Page<T> page, T example);
	
	public void findPage(final Page<T> page, QueryBuilder query);

	public T findUnique(Object... properties);
	
	public T findUnique(Map<Object, Object> properties);
	
	public T findOne(Object... properties);

}
