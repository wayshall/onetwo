package org.onetwo.dbm.jdbc;

import org.springframework.jdbc.core.SingleColumnRowMapper;

public class SingleColumnSetResultSetExtractor<T> extends SetRowMapperResultSetExtractor<T> {

	public SingleColumnSetResultSetExtractor(Class<T> columnType) {
		super(new SingleColumnRowMapper<T>(columnType));
	}

}
