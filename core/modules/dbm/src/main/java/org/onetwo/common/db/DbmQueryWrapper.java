package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("rawtypes")
public interface DbmQueryWrapper {
 
	public int executeUpdate();

	public <T> List<T> getResultList();

	public <T> T getSingleResult();

	public DbmQueryWrapper setFirstResult(int startPosition);

	public DbmQueryWrapper setMaxResults(int maxResult);

	public DbmQueryWrapper setParameter(int position, Object value);

	public DbmQueryWrapper setParameter(String name, Object value);
	
	public DbmQueryWrapper setParameters(Map<String, Object> params);
	
	public DbmQueryWrapper setParameters(List<Object> params);
	
	public DbmQueryWrapper setParameters(Object[] params);
	
	public DbmQueryWrapper setPageParameter(final Page page);
	
	public DbmQueryWrapper setLimited(final Integer first, final Integer size);
	
	public <T> T getRawQuery(Class<T> clazz);
	
	public DbmQueryWrapper setQueryConfig(Map<String, Object> configs);
	
//	public DataQuery setFlushMode(FlushModeType flushMode);
	
	/*public boolean isCacheable();

	public void setCacheable(boolean cacheable);*/
	
	public void setRowMapper(RowMapper<?> rowMapper);
}