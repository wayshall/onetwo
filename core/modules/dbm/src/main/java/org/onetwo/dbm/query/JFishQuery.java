package org.onetwo.dbm.query;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface JFishQuery {

	public JFishQuery setParameter(Integer index, Object value);

	public JFishQuery setParameter(String name, Object value);

	public <T> T getSingleResult();

	public <T> List<T> getResultList();

	public JFishQuery setFirstResult(int firstResult);

	public JFishQuery setMaxResults(int maxResults);
	
	public JFishQuery setResultClass(Class<?> resultClass);
	
	public JFishQuery setParameters(Map<String, Object> params);
	
	public JFishQuery setParameters(List<?> params);
	
	public int executeUpdate();
	
	public void setRowMapper(RowMapper<?> rowMapper);
	public void setQueryAttributes(Map<Object, Object> params);
}