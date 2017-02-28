package org.onetwo.dbm.jdbc;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class DbmListRowMapperResultSetExtractor<T> extends DbmRowMapperResultSetExtractor<List<T>, T> {

	public DbmListRowMapperResultSetExtractor(RowMapper<T> rowMapper) {
		super(rowMapper);
	}

	public DbmListRowMapperResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected) {
		super(rowMapper, rowsExpected);
	}

}
