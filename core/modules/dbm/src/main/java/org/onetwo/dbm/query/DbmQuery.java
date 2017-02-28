package org.onetwo.dbm.query;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface DbmQuery {

	public DbmQuery setParameter(Integer index, Object value);

	public DbmQuery setParameter(String name, Object value);

	public <T> T getSingleResult();

	public <T> List<T> getResultList();

	public DbmQuery setFirstResult(int firstResult);

	public DbmQuery setMaxResults(int maxResults);
	
	public DbmQuery setResultClass(Class<?> resultClass);
	
	public DbmQuery setParameters(Map<String, Object> params);
	
	public DbmQuery setParameters(List<?> params);
	
	public int executeUpdate();
	
	public void setRowMapper(RowMapper<?> rowMapper);
	public void setQueryAttributes(Map<Object, Object> params);
}