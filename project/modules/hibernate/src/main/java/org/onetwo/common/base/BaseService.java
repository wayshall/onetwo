package org.onetwo.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.IdEntity;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.utils.Page;

@SuppressWarnings("unchecked")
public interface BaseService<T extends IdEntity, PK extends Serializable> {

	public Number countRecord(Map<Object, Object> properties);

	public Number countRecord(Object... params); 

	public <T extends ILogicDeleteEntity> void delete(T entity);

	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass, PK id);

	public List<T> findAll();

	public T findById(PK id);

	public List<T> findByProperties(Map<Object, Object> properties);

	public List<T> findByProperties(Object... properties);

	public void findPage(Page page, Map<Object, Object> properties);

	public void findPage(Page page, Object... properties);

	public T findUnique(Map<Object, Object> properties);

	public T findUnique(Object... properties);

//	public T findUnique(String sql, Object... values);

	public T load(PK id);

	public void persist(T entity);

	public void remove(T entity);

	public T removeById(PK id);

	public void save(T entity);

}