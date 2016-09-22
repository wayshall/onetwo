package org.onetwo.dbm.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;

public interface RowMapperFactory {
	public RowMapper<?> createDefaultRowMapper(Class<?> type);
}
