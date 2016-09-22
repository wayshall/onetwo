package org.onetwo.dbm.query;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.AbstractDataQuery;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.utils.LangUtils;

public class JFishDataQuery extends AbstractDataQuery {
	
	private JFishQuery jfishQuery;
	
	public JFishDataQuery(JFishQuery jfishQuery) {
		super();
		this.jfishQuery = jfishQuery;
	}

	@Override
	public int executeUpdate() {
		return jfishQuery.executeUpdate();
	}

	@Override
	public <T> List<T> getResultList() {
		return jfishQuery.getResultList();
	}

	@Override
	public <T> T getSingleResult() {
		return jfishQuery.getSingleResult();
	}

	@Override
	public DataQuery setFirstResult(int startPosition) {
		jfishQuery.setFirstResult(startPosition);
		return this;
	}

	@Override
	public DataQuery setMaxResults(int maxResult) {
		jfishQuery.setMaxResults(maxResult);
		return this;
	}

	@Override
	public DataQuery setParameter(int position, Object value) {
		jfishQuery.setParameter(position, value);
		return this;
	}

	@Override
	public DataQuery setParameter(String name, Object value) {
		jfishQuery.setParameter(name, value);
		return this;
	}

	@Override
	public DataQuery setParameters(Map<String, Object> params) {
		jfishQuery.setParameters(params);
		return this;
	}

	@Override
	public DataQuery setParameters(List<Object> params) {
		jfishQuery.setParameters(params);
		return this;
	}

	@Override
	public DataQuery setParameters(Object[] params) {
		jfishQuery.setParameters(LangUtils.asList(params));
		return this;
	}


	@Override
	public DataQuery setLimited(Integer first, Integer size) {
		if (first >= 0) {
			jfishQuery.setFirstResult(first);
		}
		if (size >= 1) {
			jfishQuery.setMaxResults(size);
		}
		return this;
	}

	@Override
	public <T> T getRawQuery(Class<T> clazz) {
		return clazz.cast(jfishQuery);
	}

	@Override
	public DataQuery setQueryConfig(Map<String, Object> configs) {
		return this;
	}

	public JFishQuery getJfishQuery() {
		return jfishQuery;
	}

}
