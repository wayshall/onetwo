package org.onetwo.common.hibernate;

import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;

import org.hibernate.Query;
import org.onetwo.common.db.AbstractDataQuery;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.utils.ArrayUtils;

@SuppressWarnings("unchecked")
public class HibernateQueryImpl extends AbstractDataQuery {
 
	private Query query;
	
	public HibernateQueryImpl(Query query){
		this.query = query;
	}

	public int executeUpdate() {
		return query.executeUpdate();
	}

	public <T> List<T> getResultList() {
		query.setCacheable(isCacheable());
		return query.list();
	}

	public Object getSingleResult() {
		query.setCacheable(isCacheable());
		return query.uniqueResult();
	}

	public DataQuery setFirstResult(int startPosition) {
		query.setFirstResult(startPosition);
		return this;
	}

	public DataQuery setMaxResults(int maxResult) {
		query.setMaxResults(maxResult);
		return this;
	}

	public DataQuery setParameter(int position, Object value) {
		query.setParameter(position, value);
		return this;
	}

	public DataQuery setParameter(String name, Object value) {
		query.setParameter(name, value);
		return this;
	}

	public DataQuery setParameters(Map<String, Object> params) {
		if(params==null)
			return this;
		for(Map.Entry<String, Object> entry : params.entrySet()){
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public DataQuery setParameters(List<Object> params) {
		if(params==null || params.isEmpty())
			return this;
		int position = 1;
		for(Object value : params){
			query.setParameter(position, value);
			position++;
		}
		return this;
	}


	@Override
	public DataQuery setParameters(Object[] params) {
		if(ArrayUtils.hasNotElement(params))
			return this;
		int position = 1;
		for(Object value : params){
			query.setParameter(position++, value);
		}
		return this;
	}
	
	public DataQuery setLimited(final Integer first, final Integer max) {
		if (first >= 0) {
			query.setFirstResult(first);
		}
		if (max >= 1) {
			query.setMaxResults(max);
		}
		return this;
	}

	public DataQuery setFlushMode(FlushModeType flushMode){
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getRawQuery(Class<T> clazz) {
		return (T)query;
	}

	@Override
	public DataQuery setQueryConfig(Map<String, Object> configs) {
		return this;
	}

	
}
