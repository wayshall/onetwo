package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

public abstract class BaseEntityManagerAdapter implements BaseEntityManager {

	@Override
	public <T> List<T> selectFields(Class<?> entityClass, String[] selectFields, Object... properties) {
		throw new UnsupportedOperationException();
	}


	@Override
	public <T> List<T> select(Class<?> entityClass, Map<Object, Object> properties) {
		throw new UnsupportedOperationException();
	}
}
