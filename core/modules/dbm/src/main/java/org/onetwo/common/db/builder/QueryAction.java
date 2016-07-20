package org.onetwo.common.db.builder;

import java.util.List;

import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public interface QueryAction {

//	public ExtQuery build(Class<?> entityClass, String alias, Map<Object, Object> properties);
	
	public <T> T unique();
	
	public <T> T one();

	public <T> List<T> list();
	
	public <T> Page<T> page(Page<T> page);
	
	public <T> T extractAs(ResultSetExtractor<T> rse);
	
	public <T> List<T> listWith(RowMapper<T> rowMapper);
}
