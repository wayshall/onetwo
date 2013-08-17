package org.onetwo.common.jdbc;

import org.springframework.jdbc.core.RowMapper;

public interface RowMapperFactory {
	public RowMapper<?> createDefaultRowMapper(Class<?> type);
}
