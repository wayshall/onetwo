package org.onetwo.common.fish.spring;

import org.springframework.jdbc.core.RowMapper;

public interface RowMapperFactory {
	public <T> RowMapper<T> createDefaultRowMapper(Class<T> type);
}
