package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Page;

@SuppressWarnings("rawtypes")
public interface DataQuery {
 
	public int executeUpdate();

	public <T> List<T> getResultList();

	public Object getSingleResult();

	public DataQuery setFirstResult(int startPosition);

	public DataQuery setMaxResults(int maxResult);

	public DataQuery setParameter(int position, Object value);

	public DataQuery setParameter(String name, Object value);
	
	public DataQuery setParameters(Map<String, Object> params);
	
	public DataQuery setParameters(List<Object> params);
	
	public DataQuery setParameters(Object[] params);
	
	public DataQuery setPageParameter(final Page page);
	
	public DataQuery setLimited(final Integer first, final Integer size);
	
	public <T> T getRawQuery();
	
	public DataQuery setQueryConfig(Map<String, Object> configs);
	
//	public DataQuery setFlushMode(FlushModeType flushMode);
}