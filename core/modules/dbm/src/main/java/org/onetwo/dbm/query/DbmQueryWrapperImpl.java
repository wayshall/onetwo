package org.onetwo.dbm.query;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.AbstractDbmQueryWrapper;
import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.core.RowMapper;

public class DbmQueryWrapperImpl extends AbstractDbmQueryWrapper {
	
	private DbmQuery dbmQuery;
	
	public DbmQueryWrapperImpl(DbmQuery jfishQuery) {
		super();
		this.dbmQuery = jfishQuery;
	}

	@Override
	public int executeUpdate() {
		return dbmQuery.executeUpdate();
	}

	@Override
	public <T> List<T> getResultList() {
		return dbmQuery.getResultList();
	}

	@Override
	public <T> T getSingleResult() {
		return dbmQuery.getSingleResult();
	}

	@Override
	public DbmQueryWrapper setFirstResult(int startPosition) {
		dbmQuery.setFirstResult(startPosition);
		return this;
	}

	@Override
	public DbmQueryWrapper setMaxResults(int maxResult) {
		dbmQuery.setMaxResults(maxResult);
		return this;
	}

	@Override
	public DbmQueryWrapper setParameter(int position, Object value) {
		dbmQuery.setParameter(position, value);
		return this;
	}

	@Override
	public DbmQueryWrapper setParameter(String name, Object value) {
		dbmQuery.setParameter(name, value);
		return this;
	}

	@Override
	public DbmQueryWrapper setParameters(Map<String, Object> params) {
		dbmQuery.setParameters(params);
		return this;
	}

	@Override
	public DbmQueryWrapper setParameters(List<Object> params) {
		dbmQuery.setParameters(params);
		return this;
	}

	@Override
	public DbmQueryWrapper setParameters(Object[] params) {
		dbmQuery.setParameters(LangUtils.asList(params));
		return this;
	}


	@Override
	public DbmQueryWrapper setLimited(Integer first, Integer size) {
		if (first >= 0) {
			dbmQuery.setFirstResult(first);
		}
		if (size >= 1) {
			dbmQuery.setMaxResults(size);
		}
		return this;
	}

	@Override
	public <T> T getRawQuery(Class<T> clazz) {
		return clazz.cast(dbmQuery);
	}

	@Override
	public DbmQueryWrapper setQueryConfig(Map<String, Object> configs) {
		return this;
	}

	public DbmQuery getJfishQuery() {
		return dbmQuery;
	}

	@Override
	public void setRowMapper(RowMapper<?> rowMapper) {
		this.dbmQuery.setRowMapper(rowMapper);
	}

}
