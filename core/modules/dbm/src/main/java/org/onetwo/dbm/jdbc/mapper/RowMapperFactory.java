package org.onetwo.dbm.jdbc.mapper;

import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.springframework.jdbc.core.RowMapper;

public interface RowMapperFactory {
	public RowMapper<?> createRowMapper(Class<?> type);
	public RowMapper<?> createRowMapper(NamedQueryInvokeContext invokeContext);
	
}
