package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.exception.NotUniqueResultException;
import org.onetwo.common.utils.MyUtils;

public abstract class BaseEntityManagerAdapter implements BaseEntityManager {

	@Override
	public <T> List<T> selectFields(Class<?> entityClass, String[] selectFields, Object... properties) {
		throw new UnsupportedOperationException();
	}


	@Override
	public <T> List<T> select(Class<?> entityClass, Map<Object, Object> properties) {
		throw new UnsupportedOperationException();
	}


	public <T> T findUnique(QueryBuilder squery) {
		return findUnique((Class<T>)squery.getEntityClass(), squery.getParams());
	}

	public <T> T findUnique(Class<T> entityClass, Object... properties) {
		return this.findUnique(entityClass, MyUtils.convertParamMap(properties));
	}
	
	public <T> T findOne(Class<T> entityClass, Object... properties) {
		return this.findOne(entityClass, MyUtils.convertParamMap(properties));
	}

	public <T> T findOne(Class<T> entityClass, Map<Object, Object> properties) {
		try {
			return findUnique(entityClass, properties);
		} catch (NotUniqueResultException e) {
			return null;
		}
	}
	
}
