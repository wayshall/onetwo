package org.onetwo.dbm.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

public class SetRowMapperResultSetExtractor<T> implements ResultSetExtractor<Set<T>>{

	private final RowMapper<T> rowMapper;

	private final int rowsExpected;

	public SetRowMapperResultSetExtractor(RowMapper<T> rowMapper) {
		this(rowMapper, 0);
	}
	
	public SetRowMapperResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected) {
		Assert.notNull(rowMapper, "RowMapper is required");
		this.rowMapper = rowMapper;
		this.rowsExpected = rowsExpected;
	}

	public Set<T> extractData(ResultSet rs) throws SQLException {
		Set<T> results = (this.rowsExpected > 0 ? new HashSet<T>(this.rowsExpected) : new HashSet<T>());
		int rowNum = 0;
		while (rs.next()) {
			T row = this.rowMapper.mapRow(rs, rowNum++);
			if(row!=null){
				results.add(row);
			}
		}
		return results;
	}

}
